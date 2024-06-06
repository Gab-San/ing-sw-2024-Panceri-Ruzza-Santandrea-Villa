package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GamePoint;
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

public class TUI implements View{
    private final CommandPassthrough serverProxy;
    private static final int BACKLOG_SIZE = 10;
    private final BlockingQueue<String> inputQueue;
    private TUIParser parser;
    private ViewBoard board;
    private final List<String> notificationBacklog;
    private final List<String> chatBacklog;
    private boolean hasServerTimeoutDisconnected;
    private final boolean verbose;

    public TUI(CommandPassthrough serverProxy, Consumer<ModelUpdater> setClientModelUpdater, BlockingQueue<String> inputQueue, boolean verbose) throws RemoteException {
        SceneManager.getInstance().loadScene(SceneID.getNicknameSelectSceneID(), new PrintNicknameSelectUI());
        this.serverProxy = serverProxy;
        this.inputQueue = inputQueue;
        this.verbose = verbose;
        hasServerTimeoutDisconnected = false;
        this.notificationBacklog = Collections.synchronizedList(new LinkedList<>());
        this.chatBacklog = Collections.synchronizedList(new LinkedList<>());
        //TODO: if possible correct this, it should be in run not in the constructor
        runNicknameSelectScene(setClientModelUpdater);
        // at this point, connection has concluded successfully.

    //region Loading TUI_SCENES


        TUI_Scene boardUI = new PrintBoardUI(board);
        boardUI.setNotificationBacklog(notificationBacklog);
        boardUI.setChatBacklog(chatBacklog);
        SceneManager.getInstance().loadScene(SceneID.getBoardSceneID(), boardUI);

        TUI_Scene myAreaUI = new PrintPlayerUI(board.getPlayerHand(), board.getPlayerArea(board.getPlayerHand().getNickname()));
        myAreaUI.setNotificationBacklog(notificationBacklog);
        myAreaUI.setChatBacklog(chatBacklog);
        SceneManager.getInstance().loadScene(SceneID.getMyAreaSceneID(), myAreaUI);

        TUI_Scene endgameUI = new PrintEndgameUI(board);
        endgameUI.setNotificationBacklog(notificationBacklog);
        endgameUI.setChatBacklog(chatBacklog);
        SceneManager.getInstance().loadScene(SceneID.getEndgameSceneID(), endgameUI);

        TUI_Scene helperUI = new PrintHelperUI();
        helperUI.setNotificationBacklog(notificationBacklog);
        helperUI.setChatBacklog(chatBacklog);
        SceneManager.getInstance().loadScene(SceneID.getHelperSceneID(), helperUI);
    //endregion
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
        SceneManager.getInstance().setScene(SceneID.getNicknameSelectSceneID());
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
                    //FIXME Post event handle
                    SceneManager.getInstance().getCurrentScene().displayError("Join failed. Server can't accommodate you now.\n" + e.getMessage());
                    nickname = "";
                }
            }
            else{
                String error = UIFunctions.evaluateErrorType(nickname);
                SceneManager.getInstance().getCurrentScene().displayError(error);
            }
        }while (!validateNickname(nickname));
    }

    private void printCommandPrompt(){
        synchronized(System.out){
            System.out.print("Command > ");
        }
    }

    public void run() throws RemoteException, DisconnectException, TimeoutException {
        SceneManager.getInstance().setScene(SceneID.getMyAreaSceneID());
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

    void refreshScene(){
        SceneManager.getInstance().getCurrentScene().display();
        printCommandPrompt();
    }

    void moveView(List<CornerDirection> directions){
        SceneManager.getInstance().getCurrentScene().moveView(directions);
        printCommandPrompt();
    }
    void setCenter(int row, int col) {
        SceneManager.getInstance().getCurrentScene().setCenter(row,col);
        printCommandPrompt();
    }
    void setCenter(GamePoint center) {
        SceneManager.getInstance().getCurrentScene().setCenter(center);
        printCommandPrompt();
    }
    @Override
    public synchronized void update(SceneID sceneID, String description) {
        Scene scene = SceneManager.getInstance().getScene(sceneID);
        if(scene != null){
            Scene currentScene = SceneManager.getInstance().getCurrentScene();
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
            SceneManager.getInstance().loadScene(sceneID, opponentUI);
            update(sceneID, description); // won't loop indefinitely as next iteration will have scene != null
        }
    }
    @Override
    public synchronized void showError(String errorMsg) {
        SceneManager.getInstance().getCurrentScene().displayError(errorMsg);
        printCommandPrompt();
    }
    @Override
    public synchronized void showNotification(String notification) {
        if(notification == null || notification.isEmpty()) return;
        if(notificationBacklog.size() >= BACKLOG_SIZE){
            notificationBacklog.remove(0);
        }
        notificationBacklog.add(notification);
        SceneManager.getInstance().getCurrentScene().displayNotification(notificationBacklog);
        printCommandPrompt();
    }
    @Override
    public synchronized void showChatMessage(String msg){
        if(chatBacklog.size() >= BACKLOG_SIZE){
            chatBacklog.remove(0);
        }
        chatBacklog.add(msg);
        SceneManager.getInstance().getCurrentScene().displayChatMessage(chatBacklog);
        printCommandPrompt();
    }
    public synchronized void notifyTimeout(){
        hasServerTimeoutDisconnected = true;
        showError("You have been disconnected for timeout!");
    }

}
