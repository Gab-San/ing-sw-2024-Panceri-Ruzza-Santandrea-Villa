package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.Point;
import it.polimi.ingsw.network.CommandPassthrough;
import it.polimi.ingsw.network.Parser;
import it.polimi.ingsw.view.*;
import it.polimi.ingsw.view.model.ViewBoard;
import it.polimi.ingsw.view.model.ViewPlayArea;
import it.polimi.ingsw.view.model.ViewPlayerHand;
import it.polimi.ingsw.view.tui.scenes.PrintBoardUI;
import it.polimi.ingsw.view.tui.scenes.PrintNicknameSelectUI;
import it.polimi.ingsw.view.tui.scenes.PrintOpponentUI;
import it.polimi.ingsw.view.tui.scenes.PrintPlayerUI;

import java.rmi.RemoteException;
import java.util.*;
import java.util.function.Consumer;

public class TUI extends View{
    private static final int NOTIFICATION_BACKLOG_SIZE = 30;
    Scanner scanner;
    TUIParser parser;
    ViewBoard board;
    List<String> notificationBacklog;

    public TUI(CommandPassthrough serverProxy, Consumer<ModelUpdater> setClientModelUpdater, Scanner scanner) throws RemoteException {
        super(serverProxy, new PrintNicknameSelectUI());
        sceneIDMap.put(SceneID.getNicknameSelectSceneID(), currentScene);
        this.scanner = scanner;
        this.notificationBacklog = Collections.synchronizedList(new LinkedList<>());
        runNicknameSelectScene();
        setClientModelUpdater.accept(new ModelUpdater(board, this));

        // at this point, connection has concluded successfully.
        sceneIDMap.put(SceneID.getBoardSceneID(), new PrintBoardUI(board));
        sceneIDMap.put(SceneID.getMyAreaSceneID(), new PrintPlayerUI(board.getPlayerHand(), board.getPlayerArea(board.getPlayerHand().getNickname())));
        currentScene = sceneIDMap.get(SceneID.getMyAreaSceneID());
    }

    /**
     * Nickname must contain at least one letter and not start with a space <br>
     * It can contain any character
     * @param nickname nickname to validate
     * @return true if the nickname is valid, <br> false if not valid
     */
    private boolean validateNickname(String nickname){
        return nickname.matches("[^ ].*[a-zA-Z].*")
                && nickname.length() < Client.MAX_NICKNAME_LENGTH;
    }
    private void runNicknameSelectScene() throws RemoteException{
        currentScene.display();
        String nickname;
        do {
            nickname = scanner.nextLine();
            if(validateNickname(nickname)){
                try{
                    board = new ViewBoard(nickname);
                    parser = new TUIParser(serverProxy, this, board);
                    parser.parseCommand("connect " + nickname);
                }catch (IllegalStateException e){
                    currentScene.displayError("Join failed. Server can't accomodate you now.\n" + e.getMessage());
                    nickname = "";
                }
            }
            else currentScene.displayError("Invalid nickname, only use letters, numbers and _.");
        }while (!validateNickname(nickname));
    }

    private void printCommandPrompt(){
        synchronized(System.out){
            System.out.print("Command > ");
        }
    }

    public void run() throws RemoteException{
        refreshScene();
        while(true){ //exits only on System.quit() or RemoteException
            try {
                parser.parseCommand(scanner.nextLine());
            }catch (IllegalArgumentException | IllegalStateException e){
                showError(e.getMessage());
            }
        }
        //TODO: get command input and send to parser
    }

    private void refreshScene(){
        currentScene.display();
        printCommandPrompt();
    }

    void moveView(List<CornerDirection> directions){
        currentScene.moveView(directions);
    }
    void setCenter(int row, int col) {
        currentScene.setCenter(row,col);
    }
    void setCenter(Point center) {
        currentScene.setCenter(center);
    }
    @Override
    public void setScene(SceneID sceneID) throws IllegalArgumentException{
        Scene newScene = sceneIDMap.get(sceneID);
        if(newScene == null){
            throw new IllegalArgumentException("Scene " + sceneID + " does not exist.");
        }

        currentScene = newScene;
        refreshScene();
    }

    @Override
    public void update(SceneID sceneID, String description) {
        Scene scene = sceneIDMap.get(sceneID);
        if(scene != null){
            if(currentScene.equals(scene)){
                refreshScene();
            }
            else{
                if(description != null && !description.isEmpty())
                    showNotification(description);
            }
        }
        else if(sceneID.isOpponentAreaScene()){
            String nick = sceneID.getNickname();
            sceneIDMap.put(sceneID, new PrintOpponentUI(board.getOpponentHand(nick), board.getPlayerArea(nick)));
            update(sceneID, description); // won't loop indefinitely as next iteration will have scene != null
        }
    }
    @Override
    public void showError(String errorMsg) {
        currentScene.displayError(errorMsg);
        printCommandPrompt();
    }
    @Override
    public void showNotification(String notification) {
        if(notificationBacklog.size() >= NOTIFICATION_BACKLOG_SIZE){
            notificationBacklog.remove(0);
        }
        notificationBacklog.add(notification);
        currentScene.displayNotification(notificationBacklog);
        printCommandPrompt();
    }

}
