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
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

public class TUI extends View{
    Scanner scanner;
    TUIParser parser;
    ViewBoard board;

    public TUI(CommandPassthrough serverProxy, Consumer<ModelUpdater> setClientModelUpdater) throws RemoteException {
        super(serverProxy, new PrintNicknameSelectUI());
        sceneIDMap.put(SceneID.getNicknameSelectSceneID(), currentScene);
        scanner = new Scanner(System.in);
        runNicknameSelectScene();
        setClientModelUpdater.accept(new ModelUpdater(board, this));

        // at this point, connection has concluded successfully.
        currentScene = sceneIDMap.get(SceneID.getMyAreaSceneID());
    }

    /**
     * Nickname must contain at least one letter or '_'
     * @param nickname nickname to validate
     * @return true if the nickname is valid, <br> false if not valid (contains only numbers)
     */
    private boolean validateNickname(String nickname){
        return nickname.matches("[a-zA-Z0-9_]+")
                && !nickname.replaceAll("\\d", "").isEmpty();
    }
    private void runNicknameSelectScene() throws RemoteException{
        currentScene.display();
        String nickname;
        do {
            nickname = scanner.nextLine();
            if(validateNickname(nickname)){
                try{
                    board = new ViewBoard(nickname);
                    ViewPlayerHand myHand = board.getPlayerHand();
                    ViewPlayArea myArea = board.getPlayerArea(myHand.getNickname());
                    sceneIDMap.put(SceneID.getBoardSceneID(), new PrintBoardUI(board));
                    sceneIDMap.put(SceneID.getMyAreaSceneID(), new PrintPlayerUI(myHand, myArea));

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

    public void run() throws RemoteException{
        TUI_Scene.cls();
        currentScene.display();
        while(true){ //exits only on System.quit() or RemoteException
            try {
                parser.parseCommand(scanner.nextLine());
            }catch (IllegalArgumentException | IllegalStateException e){
                showError(e.getMessage());
            }
        }
        //TODO: get command input and send to parser
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
        currentScene.display();
    }

    @Override
    public void update(SceneID sceneID, String description) {
        Scene scene = sceneIDMap.get(sceneID);
        if(scene != null){
            if(currentScene.equals(scene)){
                currentScene.display();
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
    }

    @Override
    public void showNotification(String notification) {
        currentScene.displayNotification(notification);
    }

}
