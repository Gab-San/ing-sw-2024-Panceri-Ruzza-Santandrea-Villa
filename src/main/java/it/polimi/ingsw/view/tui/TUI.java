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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class TUI extends View{
    private static final int BACKLOG_SIZE = 10;
    private final BlockingQueue<String> inputQueue;
    private TUIParser parser;
    private ViewBoard board;
    private final List<String> notificationBacklog;
    private final List<String> chatBacklog;
    private boolean hasServerTimeoutDisconnected;
    private final boolean verbose;

    public TUI(CommandPassthrough serverProxy, Consumer<ModelUpdater> setClientModelUpdater, BlockingQueue<String> inputQueue, boolean verbose) throws RemoteException {
        super(serverProxy, new PrintNicknameSelectUI());
        sceneIDMap.put(SceneID.getNicknameSelectSceneID(), currentScene);
        this.inputQueue = inputQueue;
        this.verbose = verbose;
        hasServerTimeoutDisconnected = false;
        this.notificationBacklog = Collections.synchronizedList(new LinkedList<>());
        this.chatBacklog = Collections.synchronizedList(new LinkedList<>());
        runNicknameSelectScene(setClientModelUpdater);
        // at this point, connection has concluded successfully.

    //region TUI_Scene initalization
        TUI_Scene boardUI = new PrintBoardUI(board);
        boardUI.setNotificationBacklog(notificationBacklog);
        boardUI.setChatBacklog(chatBacklog);
        sceneIDMap.put(SceneID.getBoardSceneID(), boardUI);

        TUI_Scene myAreaUI = new PrintPlayerUI(board.getPlayerHand(), board.getPlayerArea(board.getPlayerHand().getNickname()));
        myAreaUI.setNotificationBacklog(notificationBacklog);
        myAreaUI.setChatBacklog(chatBacklog);
        sceneIDMap.put(SceneID.getMyAreaSceneID(), myAreaUI);

        TUI_Scene endgameUI = new PrintEndgameUI(board);
        endgameUI.setNotificationBacklog(notificationBacklog);
        endgameUI.setChatBacklog(chatBacklog);
        sceneIDMap.put(SceneID.getEndgameSceneID(), endgameUI);

        TUI_Scene helperUI = new PrintHelperUI();
        helperUI.setNotificationBacklog(notificationBacklog);
        helperUI.setChatBacklog(chatBacklog);
        sceneIDMap.put(SceneID.getHelperSceneID(), helperUI);
    //endregion
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
            try {
                nickname = inputQueue.take();
            }catch (InterruptedException e){
                nickname = "";
            }
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
            else{
                String error = "Invalid nickname!";
                if(nickname.length() < 3)
                    error += "\n- Use at least three characters.";
                if(nickname.length() > Client.MAX_NICKNAME_LENGTH)
                    error += "\n- Nickname is too long. Do not use more than " + Client.MAX_NICKNAME_LENGTH + " characters";
                if(!nickname.matches(".*[a-zA-Z].*"))
                    error += "\n- Nickname must contain at least one letter";
                if(!nickname.matches("[^\n ].*"))
                    error += "\n- Nickname can't begin with a space";
                if(!nickname.matches(".*[^\n ]"))
                    error += "\n- Nickname can't end with a space";
                currentScene.displayError(error);
            }
        }while (!validateNickname(nickname));
    }

    private void printCommandPrompt(){
        synchronized(System.out){
            System.out.print("Command > ");
        }
    }

    public void run() throws RemoteException, DisconnectException, TimeoutException {
        refreshScene();
        String input;
        final int SLEEP_MILLIS = 200;
        while(true) {
            try {
                input = inputQueue.poll(SLEEP_MILLIS, TimeUnit.MILLISECONDS);
                synchronized (this){
                    if(hasServerTimeoutDisconnected){
                        throw new TimeoutException();
                    }
                }
                if(input != null) {
                    parser.parseCommand(input);
                }
            } catch (InterruptedException ignored) {
            } catch (IllegalArgumentException | IllegalStateException e){
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
        printCommandPrompt();
    }
    void setCenter(int row, int col) {
        currentScene.setCenter(row,col);
        printCommandPrompt();
    }
    void setCenter(Point center) {
        currentScene.setCenter(center);
        printCommandPrompt();
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
