package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.network.CommandPassthrough;
import it.polimi.ingsw.view.*;
import it.polimi.ingsw.view.events.DisplayEvent;
import it.polimi.ingsw.view.events.GUIEvent;
import it.polimi.ingsw.view.exceptions.DisconnectException;
import it.polimi.ingsw.view.exceptions.TimeoutException;
import it.polimi.ingsw.view.gui.scenes.dialogs.choosecolor.ChooseColorScene;
import it.polimi.ingsw.view.gui.scenes.connection.ConnectionScene;
import it.polimi.ingsw.view.gui.scenes.board.BoardScene;
import it.polimi.ingsw.view.gui.scenes.dialogs.chooseobjective.ChooseObjectiveScene;
import it.polimi.ingsw.view.gui.scenes.areas.localarea.LocalPlayerAreaScene;
import it.polimi.ingsw.view.gui.scenes.areas.opponentarea.OpponentAreaScene;
import it.polimi.ingsw.view.gui.scenes.dialogs.setplayers.SetPlayersScene;
import it.polimi.ingsw.view.gui.scenes.endgame.EndgameScene;
import it.polimi.ingsw.view.model.ViewBoard;
import it.polimi.ingsw.view.model.ViewHand;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

//DOCS add docs for all gui and comment code
/**
 * This class acts as the GUI main controller class.
 * <p>
 *     This class acts as the controller for all of the events and
 *     user inputs that interact with UI elements. <br>
 *     It directly communicates with UI elements. It indirectly communicates with
 *     the view model, receiving updates and modifying the UI accordingly. Furthermore
 *     it communicates with the server thanks to the input controller,
 *     that receives the user input and after parsing, passes them
 *     through to the network interface.
 * </p>
 */
public class GameGUI implements View {
    private final BlockingQueue<String> inputQueue;
    private final GameInputHandler inputHandler;
    private final ViewBoard board;
    private GameWindow gameWindow;
    private final SceneManager sceneManager = SceneManager.getInstance();
//region EVENTS ATTRIBUTES
    private final List<JComponent> observableComponents;
    private final List<PropertyChangeListener> propertyChangeListenerList;
    private final List<ChatListener> chatListenerList;
//endregion


    /**
     * Constructs GUI.
     * @param serverProxy network interface used to communicate with the server
     * @param setClientModelUpd setter for the updater of the view
     * @param inputQueue queue for the player's input
     */
    public GameGUI(CommandPassthrough serverProxy, Consumer<ModelUpdater> setClientModelUpd, BlockingQueue<String> inputQueue){
        // Initializing View elements
        this.inputQueue = inputQueue;
        observableComponents = new LinkedList<>();
        propertyChangeListenerList = new LinkedList<>();
        chatListenerList = new LinkedList<>();

        board = new ViewBoard(this);
        addToObservableComponents(board);

        ModelUpdater modelUpdater = new ModelUpdater(board);
        setClientModelUpd.accept(modelUpdater);
        inputHandler = new GameInputHandler(serverProxy, this, new ViewController(board));
        createGUI();
        loadScenes(board);
        importFonts();
        // Subscribing all the created listeners to board events
        subscribeListenersToComponent(board);
    }


    private void importFonts() {
        // Importing inter
        try(InputStream fontIS = this.getClass().getClassLoader()
                .getResourceAsStream("fonts/inter/Inter-VariableFont_slnt,wght.ttf") ){
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            assert fontIS != null;
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, fontIS));
        } catch (IOException | FontFormatException e) {
            System.err.println(e.getMessage());
        }

        // Importing raleway
        try( InputStream fontIS = this.getClass().getClassLoader()
                .getResourceAsStream("fonts/raleway/Raleway-VariableFont_wght.ttf") ){
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            assert fontIS != null;
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, fontIS));
        } catch (IOException | FontFormatException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Loading scenes that will be displayed during the course of the game.
     * @param board board model used in board scene.
     */
    private void loadScenes(ViewBoard board) {
        // Loading connection scene
        sceneManager.loadScene(SceneID.getNicknameSelectSceneID(), new ConnectionScene(inputHandler));
        // Loading local player scene
        LocalPlayerAreaScene localPlayerAreaScene = new LocalPlayerAreaScene(inputHandler);
        sceneManager.loadScene(SceneID.getMyAreaSceneID(), localPlayerAreaScene);
        addToPropListeners(localPlayerAreaScene);
        // Loading board scene
        BoardScene boardScene = new BoardScene(board, inputHandler);
        sceneManager.loadScene(SceneID.getBoardSceneID(), boardScene);
        addToObservableComponents(boardScene);
    }

    /**
     * Updates the gui following an asynchronous event.
     * @param sceneID scene unique identifier
     * @param event event that triggered the update
     */
    @Override
    public synchronized void update(SceneID sceneID, DisplayEvent event) {
        // Executing gui events
        if (!(event instanceof GUIEvent guiEvent)) {
            return;
        }
        guiEvent.displayEvent(this);
    }

    /**
     * Updates the gui following a game phase update.
     * @param gamePhase updated game phase
     */
    public synchronized void updatePhase(GamePhase gamePhase){
        switch (gamePhase){
            case SETNUMPLAYERS:
                // Setting up and running the pop-up screen that handles user selection
                GUI_Scene setNumberOfPlayers = new SetPlayersScene(gameWindow, "Choose number of players",
                        inputHandler);
                SwingUtilities.invokeLater(
                        setNumberOfPlayers::display
                );
                break;
            case JOIN, SETUP, DEALCARDS,
                    CHOOSEFIRSTPLAYER, PLACESTARTING:
                break;
            case PLACECARD:
                showNotification("GAME PHASE UPDATED TO PLACE CARD PHASE");
                break;
            case DRAWCARD:
                showNotification("GAME PHASE UPDATED TO DRAW CARD PHASE");
                break;
            case CHOOSECOLOR:
                // Setting up and displaying the pop-up screen that handles user selection
                ChooseColorScene chooseColorScene = new ChooseColorScene(gameWindow, "Choose your color!", inputHandler);
                // Listening only to player color events
                for (JComponent component : observableComponents) {
                    if (component instanceof ViewHand) {
                        component.addPropertyChangeListener(ChangeNotifications.COLOR_CHANGE,
                                chooseColorScene);
                    }
                }
                SwingUtilities.invokeLater(
                        chooseColorScene::display
                );
                break;
            case CHOOSEOBJECTIVE:
                // Setting up and displaying the pop-up screen that handles user selection
                ChooseObjectiveScene chooseObjectiveScene = new ChooseObjectiveScene(gameWindow,
                        "Choose your objective!", inputHandler);
                // Listening to chosen objective event to close the pop-up
                for (JComponent component : observableComponents) {
                    if (component instanceof ViewHand &&
                            inputHandler.isLocalPlayer(((ViewHand) component).getNickname())) {
                        component.addPropertyChangeListener(ChangeNotifications.CHOSEN_OBJECTIVE_CARD, chooseObjectiveScene);
                    }
                }
                SwingUtilities.invokeLater(
                        chooseObjectiveScene::display
                );
                break;
            case EVALOBJ: break;
            case SHOWWIN:
                EndgameScene endgameScene = new EndgameScene(board, inputHandler);
                SceneManager.getInstance().loadScene(SceneID.getEndgameSceneID(), endgameScene);
                gameWindow.addEndgameButtonToSidePanel("Display Winner");
                changeScene(endgameScene);
                break;
        }
    }

    /**
     * Invokes the specified scene to be displayed next.
     * @param nextScene scene to be next
     */
    public synchronized void changeScene(GUI_Scene nextScene) {
        assert nextScene instanceof JPanel;
        // All scenes to display on the main frame will be JPanels
        // if a scene that is not a JPanel is issued there is an error
        // either in the design or in the execution
        SwingUtilities.invokeLater(
                () -> gameWindow.displayScene(nextScene)
        );
    }


    @Override
    public synchronized void showError(String errorMsg) {
        SceneManager.getInstance().getCurrentScene().displayError(errorMsg);
    }

    @Override
    public synchronized void showChatMessage(String messenger, String msg) {
        for(ChatListener listener : chatListenerList) {
            listener.displayMessage(messenger, msg);
        }
    }

    @Override
    public synchronized void showNotification(String notification) {
        SceneManager.getInstance().getCurrentScene().displayNotification(Collections.singletonList(notification));
    }

    //region NETWORK NOTIFICATIONS
    @Override
    public synchronized void notifyTimeout() {
        Scene currentScene = SceneManager.getInstance().getCurrentScene();
        SwingUtilities.invokeLater(
                () -> currentScene.displayError("Disconnecting for being idle!")
        );
        inputQueue.add("IDLE_DISCONNECTION");
    }

    /**
     * Notifies of a network error occurring and closes the application.
     */
    public void notifyServerFailure() {
        inputQueue.add("SERVER_FAILURE");
    }
//endregion

    private void RunNicknameScene(){
        sceneManager.setScene(SceneID.getNicknameSelectSceneID());
    }

    @Override
    public void run() throws RemoteException, TimeoutException, DisconnectException {
        SwingUtilities.invokeLater(
                this::RunNicknameScene
        );
        final int SLEEP_MILLIS = 200;
        try {
            while (true) {
                String message;
                message = inputQueue.poll(SLEEP_MILLIS, TimeUnit.MILLISECONDS);
                if(message != null) {
                    switch (message) {
                        case "SERVER_FAILURE":
                            gameWindow.dispose();
                            throw new RemoteException();
                        case "IDLE_DISCONNECTION":
                            gameWindow.dispose();
                            throw new TimeoutException();
                    }
                }
            }
        } catch (InterruptedException interruptedException){
            interruptedException.printStackTrace(System.err);
        }
    }

    private void createGUI() {
        gameWindow = new GameWindow(inputHandler);
        addToPropListeners(gameWindow);
    }
//region LISTENERS' METHODS

    /**
     * Subscribe the specified property change listener to
     * all the registered components.
     * @param pcl property change listener to subscribe to events
     */
    public void subscribeToComponents(PropertyChangeListener pcl) {
        synchronized (observableComponents){
            for(JComponent component : observableComponents){
                component.addPropertyChangeListener(pcl);
            }
        }
    }

    /**
     * Subscribes all the property listeners to the specified component events.
     * @param component component of which to listen event
     */
    private void subscribeListenersToComponent(JComponent component) {
        synchronized (propertyChangeListenerList){
            for(PropertyChangeListener pcl : propertyChangeListenerList) {
                component.addPropertyChangeListener(pcl);
            }
        }
    }

    /**
     * Adds the components to the list of observable components.
     * @param component component to subscribe
     */
    private void addToObservableComponents(JComponent component){
        synchronized (observableComponents){
            observableComponents.add(component);
        }
    }

    /**
     * Adds specified object to the list of property listeners.
     * @param pcl listener to add to the list
     */
    public void addToPropListeners(PropertyChangeListener pcl) {
        synchronized (propertyChangeListenerList){
            propertyChangeListenerList.add(pcl);
        }
    }

    /**
     * Adds a chat listener to the list of chat listeners.
     * @param chatListener added listener
     */
    public void addChatListener(ChatListener chatListener){
        synchronized (chatListenerList) {
            chatListenerList.add(chatListener);
        }
    }

    /**
     * Removes the component from the observable components and
     * unsubscribes all listeners.
     * @param component component to unsubscribe
     */
    private void removeFromObservableComponents(JComponent component){
        synchronized (observableComponents){
            observableComponents.remove(component);
        }
        List<PropertyChangeListener> compPCLs = List.of(component.getPropertyChangeListeners());
        for(PropertyChangeListener pcl : compPCLs){
            component.removePropertyChangeListener(pcl);
        }
    }

    /**
     * Unsubscribes listener from all the components.
     * @param pcl listener to unsubscribe
     */
    private void unsubscribeListenerFromComponents(PropertyChangeListener pcl) {
        synchronized (observableComponents){
            for(JComponent component : observableComponents){
                component.removePropertyChangeListener(pcl);
            }
        }
    }
//endregion

    /**
     * Creates added player scene and subscribes listeners to it.
     * @param nickname added player unique id
     * @param isLocalPlayer true if it is the local player, false otherwise
     */
    public void addPlayerScene(String nickname, boolean isLocalPlayer) {
        // Must connect updates to my scene area after
        // the player has connected
        if(isLocalPlayer){
            JComponent localHand = inputHandler.getPlayerHand(nickname);
            // Adds local scene as listener to the local player's hand
            synchronized (observableComponents) {
                if (observableComponents.contains(localHand)) {
                    return;
                }
            }
            addToObservableComponents(localHand);
            return;
        }

        // Adds opponent scene as listener for opponent's hand
        ViewHand opponentHand = inputHandler.getPlayerHand(nickname);
        synchronized (observableComponents) {
            if (observableComponents.contains(opponentHand)) {
                return;
            }
        }
        // Creating opposing player's area
        // the player has connected
        OpponentAreaScene opponentScene = new OpponentAreaScene(inputHandler, opponentHand.getNickname());
        SceneManager.getInstance().loadScene(SceneID.getOpponentAreaSceneID(nickname), opponentScene);
        board.addPropertyChangeListener(opponentScene);
        addToObservableComponents(opponentHand);

        board.notifyAddedOpponent(opponentHand);
    }

    /**
     * Removes a player's scene. Triggered by player's removal event.
     * @param nickname unique identifier of the removed player
     */
    public void removePlayerScene(String nickname) {
        SceneID sceneID = SceneID.getOpponentAreaSceneID(nickname);
        OpponentAreaScene opponentAreaScene = (OpponentAreaScene) SceneManager.getInstance().getScene(sceneID);
        if(opponentAreaScene == null){
            System.out.println("THIS CALL SHOULD BE ERRONEOUS!");
            return;
        }
        GUI_Scene currentScene = (GUI_Scene) SceneManager.getInstance().getCurrentScene();
        // if the user is on the opponent area scene than
        // it gets moved to the local player's area scene
        if(opponentAreaScene == currentScene){
            SceneID mainSceneId = SceneID.getMyAreaSceneID();
            GUI_Scene nextScene = (GUI_Scene) SceneManager.getInstance().getScene(mainSceneId);
            changeScene(nextScene);
            JComponent opponentArea = inputHandler.getPlayerHand(nickname);
            removeFromObservableComponents(opponentArea);
            unsubscribeListenerFromComponents(opponentAreaScene);
        }
    }
}
