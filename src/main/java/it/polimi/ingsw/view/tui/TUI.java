package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.Point;
import it.polimi.ingsw.network.CommandPassthrough;
import it.polimi.ingsw.view.*;
import it.polimi.ingsw.view.exceptions.DisconnectException;
import it.polimi.ingsw.view.exceptions.TimeoutException;
import it.polimi.ingsw.view.model.ViewBoard;
import it.polimi.ingsw.view.tui.scenes.*;

import java.rmi.RemoteException;
import java.util.*;
import java.util.function.Consumer;

public class TUI extends View{
    private static final int BACKLOG_SIZE = 30;
    private final Scanner scanner;
    private TUIParser parser;
    private ViewBoard board;
    private final List<String> notificationBacklog;
    private final List<String> chatBacklog;
    Boolean hasServerTimeoutDisconnected;
    private final boolean verbose;

    public TUI(CommandPassthrough serverProxy, Consumer<ModelUpdater> setClientModelUpdater, Scanner scanner, boolean verbose) throws RemoteException {
        super(serverProxy, new PrintNicknameSelectUI());
        sceneIDMap.put(SceneID.getNicknameSelectSceneID(), currentScene);
        this.scanner = scanner;
        this.verbose = verbose;
        hasServerTimeoutDisconnected = false;
        this.notificationBacklog = Collections.synchronizedList(new LinkedList<>());
        this.chatBacklog = Collections.synchronizedList(new LinkedList<>());
        runNicknameSelectScene(setClientModelUpdater);
        // at this point, connection has concluded successfully.
        TUI_Scene boardUI = new PrintBoardUI(board);
        boardUI.setNotificationBacklog(notificationBacklog);
        boardUI.setChatBacklog(chatBacklog);
        sceneIDMap.put(SceneID.getBoardSceneID(), boardUI);
        TUI_Scene myAreaUI = new PrintPlayerUI(board.getPlayerHand(), board.getPlayerArea(board.getPlayerHand().getNickname()));
        myAreaUI.setNotificationBacklog(notificationBacklog);
        myAreaUI.setChatBacklog(chatBacklog);
        sceneIDMap.put(SceneID.getMyAreaSceneID(), myAreaUI);
        currentScene = myAreaUI;
    }

    /**
     * Nickname must contain at least one letter and neither start nor end with a space <br>
     * It can contain any character, minimum length of 3 characters
     * @param nickname nickname to validate
     * @return true if the nickname is valid, <br> false if not valid
     */
    private boolean validateNickname(String nickname){
        return nickname.matches("[^\n ].*[a-zA-Z].*[^\n ]")
                && nickname.length() < Client.MAX_NICKNAME_LENGTH;
    }
    private void runNicknameSelectScene(Consumer<ModelUpdater> setClientModelUpdater) throws RemoteException{
        currentScene.display();
        String nickname;
        do {
            nickname = scanner.nextLine();
            if(validateNickname(nickname)){
                try{
                    board = new ViewBoard(nickname);
                    parser = new TUIParser(serverProxy, this, board);
                    setClientModelUpdater.accept(new ModelUpdater(board, this));
                    parser.parseCommand("connect " + nickname);
                }catch (IllegalStateException | DisconnectException e){
                    currentScene.displayError("Join failed. Server can't accommodate you now.\n" + e.getMessage());
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

    public void run() throws RemoteException, DisconnectException, TimeoutException {
        refreshScene();
        while(true){ //exits only on System.quit() or RemoteException
            try {
                String input = scanner.nextLine();
                synchronized (this) {
                    if (hasServerTimeoutDisconnected) {
                        throw new TimeoutException();
                    }
                }
                parser.parseCommand(input);
            }catch (IllegalArgumentException | IllegalStateException e){
                showError(e.getMessage());
            }
        }
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
        currentScene.display();
        printCommandPrompt();
    }

    @Override
    public synchronized void update(SceneID sceneID, String description) {
        Scene scene = sceneIDMap.get(sceneID);
        if(scene != null){
            if(currentScene.equals(scene) && !verbose){
                refreshScene();
            }
            else{
                showNotification(description);
            }
        }
        else if(sceneID.isOpponentAreaScene()){
            String nick = sceneID.getNickname();
            TUI_Scene opponentUI = new PrintOpponentUI(board.getOpponentHand(nick), board.getPlayerArea(nick));
            opponentUI.setNotificationBacklog(notificationBacklog);
            opponentUI.setChatBacklog(chatBacklog);
            sceneIDMap.put(sceneID, opponentUI);
            update(sceneID, description); // won't loop indefinitely as next iteration will have scene != null
        }
    }
    @Override
    public synchronized void showError(String errorMsg) {
        currentScene.displayError(errorMsg);
        printCommandPrompt();
    }
    @Override
    public synchronized void showNotification(String notification) {
        if(notification == null || notification.isEmpty()) return;
        if(notificationBacklog.size() >= BACKLOG_SIZE){
            notificationBacklog.remove(0);
        }
        notificationBacklog.add(notification);
        currentScene.displayNotification(notificationBacklog);
        printCommandPrompt();
    }
    @Override
    public synchronized void showChatMessage(String msg){
        if(chatBacklog.size() >= BACKLOG_SIZE){
            chatBacklog.remove(0);
        }
        chatBacklog.add(msg);
        currentScene.displayChatMessage(chatBacklog);
        printCommandPrompt();
    }
    public synchronized void notifyTimeout(){
        hasServerTimeoutDisconnected = true;
        showError("You have been disconnected for timeout!");
    }

}
