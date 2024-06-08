package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.network.CommandPassthrough;
import it.polimi.ingsw.view.*;
import it.polimi.ingsw.view.events.DisplayEvent;
import it.polimi.ingsw.view.events.TUIEvent;
import it.polimi.ingsw.view.exceptions.DisconnectException;
import it.polimi.ingsw.view.exceptions.TimeoutException;
import it.polimi.ingsw.view.model.ViewBoard;
import it.polimi.ingsw.view.tui.scenes.*;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class TUI implements View{
    private static final int BACKLOG_SIZE = 10;
    private final BlockingQueue<String> inputQueue;
    private final ViewBoard board;
    private final TUIParser parser;
    private final List<String> notificationBacklog;
    private final List<String> chatBacklog;
    private boolean hasServerTimeoutDisconnected;
    private Timer refreshTimer = new Timer();
    private static final long REFRESH_TIMEOUT_MILLIS = 500L;

    public TUI(CommandPassthrough serverProxy, Consumer<ModelUpdater> setClientModelUpdater, BlockingQueue<String> inputQueue) throws RemoteException {

        this.board = new ViewBoard(this);
        ModelUpdater modelUpdater = new ModelUpdater(board);
        setClientModelUpdater.accept(modelUpdater);
        parser = new TUIParser(serverProxy, this, board);

        this.inputQueue = inputQueue;
        hasServerTimeoutDisconnected = false;
        this.notificationBacklog = Collections.synchronizedList(new LinkedList<>());
        this.chatBacklog = Collections.synchronizedList(new LinkedList<>());

        loadScenes();
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
    private void runNicknameSelectScene() throws RemoteException{
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
                    // If connection is through
                    board.addLocalPlayer(nickname);
                    parser.setSelfPlayerArea();
                    parser.parseCommand("connect " + nickname);
                }catch (IllegalStateException | DisconnectException e){
                    //FIXME Post event handle [Ale] ???
                    SceneManager.getInstance().getCurrentScene().displayError("Join failed. Server can't accommodate you now.\n" + e.getMessage());
                    nickname = "";
                }
            }
            else{
                String error = UIFunctions.evaluateErrorType(nickname);
                SceneManager.getInstance().getCurrentScene().displayError(error);
            }
        }while (!validateNickname(nickname));
        TUI_Scene myAreaUI = new PrintPlayerUI(board.getPlayerHand(),
                board.getPlayerArea(board.getPlayerHand().getNickname()));
        myAreaUI.setNotificationBacklog(notificationBacklog);
        myAreaUI.setChatBacklog(chatBacklog);
        SceneManager.getInstance().loadScene(SceneID.getMyAreaSceneID(), myAreaUI);
        SceneManager.getInstance().setScene(SceneID.getMyAreaSceneID());
    }

    private void loadScenes(){
        SceneManager.getInstance().loadScene(SceneID.getNicknameSelectSceneID(), new PrintNicknameSelectUI());
        SceneManager.getInstance().loadScene(SceneID.getNotificationSceneID(), new NotificationScene());

        TUI_Scene boardUI = new PrintBoardUI(board);
        setBacklogs(boardUI);
        SceneManager.getInstance().loadScene(SceneID.getBoardSceneID(), boardUI);

        TUI_Scene helperUI = new PrintHelperUI();
        setBacklogs(helperUI);
        SceneManager.getInstance().loadScene(SceneID.getHelperSceneID(), helperUI);
    }

    public void run() throws RemoteException, DisconnectException, TimeoutException {
        runNicknameSelectScene();
        // at this point, connection has concluded successfully.
        String input;
        final int SLEEP_MILLIS = 200;
        while(true) {
            try {
                input = inputQueue.poll(SLEEP_MILLIS, TimeUnit.MILLISECONDS);
                synchronized (this){
                    if(hasServerTimeoutDisconnected){
                        refreshTimer.cancel();
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


    void moveView(List<CornerDirection> directions){
        SceneManager.getInstance().getCurrentScene().moveView(directions);
    }
    void setCenter(int row, int col) {
        SceneManager.getInstance().getCurrentScene().setCenter(row,col);
    }


    @Override
    public synchronized void update(SceneID sceneID, DisplayEvent event) {
        Scene scene = SceneManager.getInstance().getScene(sceneID);
        if(scene != null){
            if(!(event instanceof TUIEvent tuiEvent)){
                return;
            }
            tuiEvent.displayEvent(this);
        }
        else if(sceneID.isOpponentAreaScene()){
            String nick = sceneID.getNickname();
            TUI_Scene opponentUI = new PrintOpponentUI(board.getOpponentHand(nick), board.getPlayerArea(nick));
            opponentUI.setNotificationBacklog(notificationBacklog);
            opponentUI.setChatBacklog(chatBacklog);
            SceneManager.getInstance().loadScene(sceneID, opponentUI);
            update(sceneID, event); // won't loop indefinitely as next iteration will have scene != null
        }
    }
    @Override
    public synchronized void showError(String errorMsg) {
        SceneManager.getInstance().getCurrentScene().displayError(errorMsg);
    }

    private void setRefreshTimer(){
        refreshTimer.cancel();
        refreshTimer = new Timer(true);
        refreshTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                SceneManager.getInstance().getCurrentScene().display();
            }
        }, REFRESH_TIMEOUT_MILLIS);
    }

    public synchronized void showNotification(String notification) {
        if(notification == null || notification.isEmpty()) return;
        if(notificationBacklog.size() >= BACKLOG_SIZE){
            notificationBacklog.remove(0);
        }
        notificationBacklog.add(notification);
        setRefreshTimer();
    }
    @Override
    public synchronized void showChatMessage(String msg){
        if(chatBacklog.size() >= BACKLOG_SIZE){
            chatBacklog.remove(0);
        }
        chatBacklog.add(msg);
        setRefreshTimer();
    }

    public synchronized void notifyTimeout(){
        hasServerTimeoutDisconnected = true;
        showError("You have been disconnected for timeout!");
    }

    public void setBacklogs(TUI_Scene scene){
        scene.setNotificationBacklog(notificationBacklog);
        scene.setChatBacklog(chatBacklog);
    }

}
