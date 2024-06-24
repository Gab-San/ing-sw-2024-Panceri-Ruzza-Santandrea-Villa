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

/**
 * Main class of the TUI.
 */
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

    /**
     * Constructs the TUI
     * @param serverProxy proxy to the game server to which commands should be sent to
     * @param setClientModelUpdater setter consumer for the VirtualClient's modelUpdater
     * @param inputQueue the queue from which input will be read
     */
    public TUI(CommandPassthrough serverProxy, Consumer<ModelUpdater> setClientModelUpdater, BlockingQueue<String> inputQueue){
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

    /**
     * Executes the scene that requires user to input a valid nickname. <br>
     * Attempts the connection and requests another input on join failure. <br>
     * Advances to myAreaUI on a successful join.
     * @throws RemoteException if a connection error occurs while attempting to join game
     */
    private void runNicknameSelectScene() throws RemoteException{
        SceneManager.getInstance().loadScene(SceneID.getNicknameSelectSceneID());
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
        setBacklogs(myAreaUI);
        SceneManager.getInstance().saveScene(SceneID.getMyAreaSceneID(), myAreaUI);
        SceneManager.getInstance().loadScene(SceneID.getMyAreaSceneID());
    }

    /**
     * Creates and loads the main TUI scenes in the SceneManager
     */
    private void loadScenes(){
        SceneManager.getInstance().saveScene(SceneID.getNicknameSelectSceneID(), new PrintNicknameSelectUI());
        SceneManager.getInstance().saveScene(SceneID.getNotificationSceneID(), new NotificationScene());

        TUI_Scene boardUI = new PrintBoardUI(board);
        setBacklogs(boardUI);
        SceneManager.getInstance().saveScene(SceneID.getBoardSceneID(), boardUI);

        TUI_Scene helperUI = new PrintHelperUI();
        setBacklogs(helperUI);
        SceneManager.getInstance().saveScene(SceneID.getHelperSceneID(), helperUI);
    }

    /**
     * Launches the TUI
     * @throws RemoteException if a connection error occurs while communicating with the server
     * @throws DisconnectException if the disconnect command is input and the disconnection succeeds
     * @throws TimeoutException if the server disconnects the client for timeout
     */
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

    /**
     * Calls moveView on the SceneManager currentScene
     * @param directions list of direction to move towards
     */
    void moveView(List<CornerDirection> directions){
        ((TUI_Scene) SceneManager.getInstance().getCurrentScene()).moveView(directions);
    }
    /**
     * Calls setCenter on the SceneManager currentScene
     * @param col x coordinate of the new center
     * @param row y coordinate of the new center
     */
    void setCenter(int col, int row) {
        ((TUI_Scene) SceneManager.getInstance().getCurrentScene()).setCenter(row,col);
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
            String nick = sceneID.getOpponentNickname();
            TUI_Scene opponentUI = new PrintOpponentUI(board.getOpponentHand(nick), board.getPlayerArea(nick));
            setBacklogs(opponentUI);
            SceneManager.getInstance().saveScene(sceneID, opponentUI);
            update(sceneID, event); // won't loop indefinitely as next iteration will have scene != null
        }
    }
    @Override
    public synchronized void showError(String errorMsg) {
        SceneManager.getInstance().getCurrentScene().displayError(errorMsg);
    }

    /**
     * Sets a timer to refresh the currentScene in 500 ms. <br>
     * Resets the timer if one was already running. <br><br>
     * This method prevents excessive flashing on the screen by limiting
     * refresh rate to at most twice a second.
     */
    public synchronized void setRefreshTimer(){
        refreshTimer.cancel();
        refreshTimer = new Timer(true);
        refreshTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                SceneManager.getInstance().getCurrentScene().display();
            }
        }, REFRESH_TIMEOUT_MILLIS);
    }

    /**
     * Adds a notification to the backlog and refreshes the scene
     * (using the refresh timer)
     * @param notification the text notification to be displayed
     */
    @Override
    public synchronized void showNotification(String notification) {
        if(notification == null || notification.isEmpty()) return;
        if(notificationBacklog.size() >= BACKLOG_SIZE){
            notificationBacklog.remove(0);
        }
        notificationBacklog.add(notification);
        setRefreshTimer();
    }
    @Override
    public synchronized void showChatMessage(String messenger, String msg){
        String message = messenger + "> " + msg;
        if(chatBacklog.size() >= BACKLOG_SIZE){
            chatBacklog.remove(0);
        }
        chatBacklog.add(message);
        setRefreshTimer();
    }

    /**
     * Notifies the timeout to the TUI (to later raise the TimeoutException in run())
     * and displays the timeout disconnection as an error.
     */
    public synchronized void notifyTimeout(){
        hasServerTimeoutDisconnected = true;
        showError("You have been disconnected for timeout!");
    }
    /**
     * Sets both backlog of a scene to this TUI's backlogs
     * @param scene any TUI scene
     */
    public void setBacklogs(TUI_Scene scene){
        scene.setNotificationBacklog(notificationBacklog);
        scene.setChatBacklog(chatBacklog);
    }

}
