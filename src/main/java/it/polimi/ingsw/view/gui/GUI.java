package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.network.CommandPassthrough;
import it.polimi.ingsw.view.*;
import it.polimi.ingsw.view.events.DisplayEvent;
import it.polimi.ingsw.view.events.GUIEvent;
import it.polimi.ingsw.view.exceptions.DisconnectException;
import it.polimi.ingsw.view.exceptions.TimeoutException;
import it.polimi.ingsw.view.gui.scenes.choosecolor.ChooseColorScene;
import it.polimi.ingsw.view.gui.scenes.connection.ConnectionScene;
import it.polimi.ingsw.view.gui.scenes.game.BoardScene;
import it.polimi.ingsw.view.gui.scenes.localarea.LocalPlayerAreaScene;
import it.polimi.ingsw.view.gui.scenes.setplayers.SetPlayersScene;
import it.polimi.ingsw.view.model.ViewBoard;
import it.polimi.ingsw.view.model.ViewHand;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * This class acts as the GUI main controller class.
 * <p>
 *     Stuff about a mini MVC with GUI being C and being the first
 *     and only effective frame.
 * </p>
 */
public class GUI implements View {
    private final BlockingQueue<String> inputQueue;
    private final GameInputHandler inputHandler;
    private GameWindow gameWindow;
    private final SceneManager sceneManager = SceneManager.getInstance();
    private final List<JComponent> observableComponents;
    private final List<PropertyChangeListener> propertyChangeListenerList;
    private final List<ChatListener> chatListenerList;
    private GUI_Scene lastOpenedScene;
    public GUI(CommandPassthrough serverProxy, Consumer<ModelUpdater> setClientModelUpd, BlockingQueue<String> inputQueue){
        // Initializing View elements
        this.inputQueue = inputQueue;
        observableComponents = new LinkedList<>();
        propertyChangeListenerList = new LinkedList<>();
        chatListenerList = new LinkedList<>();

        ViewBoard board = new ViewBoard(this);
        addToObservableComponents(board);

        ModelUpdater modelUpdater = new ModelUpdater(board);
        setClientModelUpd.accept(modelUpdater);
        inputHandler = new GameInputHandler(serverProxy, this, new ViewController(board));
        loadScenes();
        createGUI();
        subscribeListenersToComponent(board);
    }

    /**
     * Loading scenes that will be displayed during the course of the game.
     */
    private void loadScenes() {
        sceneManager.loadScene(SceneID.getNicknameSelectSceneID(), new ConnectionScene(inputHandler));
        sceneManager.loadScene(SceneID.getMyAreaSceneID(), new LocalPlayerAreaScene());
        sceneManager.loadScene(SceneID.getBoardSceneID(), new BoardScene());
    }

    /**
     * Updates the gui following an asynchronous event.
     * @param sceneID scene unique identifier
     * @param event event that triggered the update
     */
    @Override
    public synchronized void update(SceneID sceneID, DisplayEvent event) {
//        Scene scene = sceneManager.getScene(sceneID);
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
                    GUI_Scene setNumberOfPlayers = new SetPlayersScene(gameWindow, "Choose your objective",
                            inputHandler);
                    SwingUtilities.invokeLater(
                            setNumberOfPlayers::display
                    );
                    lastOpenedScene = setNumberOfPlayers;
                    break;
            case JOIN, SETUP, DEALCARDS,
                    CHOOSEFIRSTPLAYER, PLACECARD, DRAWCARD:
                break;
            case PLACESTARTING:
                // I really don't like this method but nothing else seems to work
                if(lastOpenedScene != null){
                    SwingUtilities.invokeLater(
                            lastOpenedScene::close
                    );
                    lastOpenedScene = null;
                }
                break;
            case CHOOSECOLOR:
                GUI_Scene chooseColorScene = new ChooseColorScene(gameWindow, "Choose your color!", inputHandler);
                SwingUtilities.invokeLater(
                        () -> {
                            for (JComponent component : observableComponents) {
                                if (component instanceof ViewHand) {
                                    component.addPropertyChangeListener(ChangeNotifications.COLOR_CHANGE,
                                            (PropertyChangeListener) chooseColorScene);
                                }
                            }
                            chooseColorScene.display();
                        }
                );
                lastOpenedScene = chooseColorScene;
                break;
            case CHOOSEOBJECTIVE:
                if(lastOpenedScene != null){
                    SwingUtilities.invokeLater(
                        lastOpenedScene::close
                    );
                    lastOpenedScene = null;
                }
                break;
            case EVALOBJ, SHOWWIN:

        }
    }

    /**
     * Invokes the specified scene to be displayed next.
     * @param nextScene scene to be next
     */
    public synchronized void changeScene(GUI_Scene nextScene) {
        System.out.println("Displaying " + nextScene);
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

    }

    @Override
    public synchronized void showChatMessage(String messenger, String msg) {
        for(ChatListener listener : chatListenerList) {
            listener.displayMessage(messenger, msg);
        }
    }

    @Override
    public synchronized void notifyTimeout() {

    }

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
                synchronized (inputQueue) {
                     message = inputQueue.poll(SLEEP_MILLIS, TimeUnit.MILLISECONDS);
                }
                if(message != null) {
                    // all of these will be error messages
                    gameWindow.dispose();
                    switch (message) {
                        case "SERVER_FAILURE":
                            throw new RemoteException();
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

    public void subscribeToComponents(PropertyChangeListener pcl) {
        synchronized (observableComponents){
            for(JComponent component : observableComponents){
                component.addPropertyChangeListener(pcl);
            }
        }
    }

    public void notifyServerFailure() {
        synchronized (inputQueue) {
            inputQueue.add("SERVER_FAILURE");
            inputQueue.notifyAll();
        }
    }

    private void subscribeListenersToComponent(JComponent component) {
        synchronized (propertyChangeListenerList){
            for(PropertyChangeListener pcl : propertyChangeListenerList) {
                component.addPropertyChangeListener(pcl);
            }
        }
    }
    private void removeFromObservableComponents(JComponent component){
        synchronized (observableComponents){
            observableComponents.remove(component);
        }
        List<PropertyChangeListener> compPCLs = List.of(component.getPropertyChangeListeners());
        for(PropertyChangeListener pcl : compPCLs){
            component.removePropertyChangeListener(pcl);
        }
    }

    private void addToObservableComponents(JComponent component){
        synchronized (observableComponents){
            observableComponents.add(component);
        }
    }


    public void addToPropListeners(PropertyChangeListener pcl) {
        synchronized (propertyChangeListenerList){
            propertyChangeListenerList.add(pcl);
        }
    }

    public void addChatListener(ChatListener chatListener){
        synchronized (chatListenerList) {
            chatListenerList.add(chatListener);
        }
    }

}
