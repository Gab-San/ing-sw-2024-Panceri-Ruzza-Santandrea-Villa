package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.network.CommandPassthrough;
import it.polimi.ingsw.view.*;
import it.polimi.ingsw.view.events.DisplayEvent;
import it.polimi.ingsw.view.events.GUIEvent;
import it.polimi.ingsw.view.exceptions.DisconnectException;
import it.polimi.ingsw.view.exceptions.TimeoutException;
import it.polimi.ingsw.view.gui.scenes.choosecolor.ChooseColorScene;
import it.polimi.ingsw.view.gui.scenes.extra.ChatPanel;
import it.polimi.ingsw.view.gui.scenes.extra.PlayerListPanel;
import it.polimi.ingsw.view.gui.scenes.localarea.PlayerAreaScene;
import it.polimi.ingsw.view.gui.scenes.connection.ConnectionScene;
import it.polimi.ingsw.view.gui.scenes.setplayers.SetPlayersScene;
import it.polimi.ingsw.view.model.ViewBoard;
import it.polimi.ingsw.view.model.ViewHand;

import javax.swing.*;
import java.awt.*;
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
public class GUI extends JFrame implements View {
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
    private final BlockingQueue<String> inputQueue;
    private final GameInputHandler inputHandler;
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
        ModelUpdater modelUpdater = new ModelUpdater(board);
        setClientModelUpd.accept(modelUpdater);
        inputHandler = new GameInputHandler(serverProxy, this, new ViewController(board));
        loadScenes();
    }

    /**
     * Loading scenes that will be displayed during the course of the game.
     */
    private void loadScenes() {
        sceneManager.loadScene(SceneID.getNicknameSelectSceneID(), new ConnectionScene(inputHandler, this));
        sceneManager.loadScene(SceneID.getMyAreaSceneID(), new PlayerAreaScene());
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
                    GUI_Scene setNumberOfPlayers = new SetPlayersScene(this, "Choose your objective",
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
                GUI_Scene chooseColorScene = new ChooseColorScene(this, "Choose your color!", inputHandler);
                SwingUtilities.invokeLater(
                        () -> {
                            for (JComponent component : observableComponents) {
                                if (component instanceof ViewHand) {
                                    component.addPropertyChangeListener(ViewHand.COLOR_PROPERTY,
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
    public synchronized void displayNextScene(GUI_Scene nextScene) {
        assert nextScene instanceof JPanel;
        // All scenes to display on the main frame will be JPanels
        // if a scene that is not a JPanel is issued there is an error
        // either in the design or in the execution
        this.add((JPanel) nextScene, BorderLayout.CENTER);
        // Sets and executes the scene
        sceneManager.setScene(nextScene);
        // This needs to be called each time a scene change
        // is issued to be sure that all the items are validated for display
        this.setVisible(true);
    }

    /**
     * Invokes the scene identified by the unique sceneId to be displayed next.
     * @param nextSceneID next scene identifier
     */
    public synchronized void displayNextScene(SceneID nextSceneID) {
        GUI_Scene nextScene = (GUI_Scene) sceneManager.getScene(nextSceneID);
        displayNextScene(nextScene);
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
    private void createGUI(){
        // Standard JFrame stuff defining window size, position and layout
        GUIFunc.setupFrame(this, "Codex Naturalis",
                SCREEN_WIDTH, SCREEN_HEIGHT);
        setLayout(new BorderLayout());

        JPanel leftSidePanel = setupLeftPanel();
        add(leftSidePanel, BorderLayout.WEST);
    }

    private JPanel setupLeftPanel() {
        ChatPanel chatPanel = new ChatPanel(inputHandler);
        addChatListener(chatPanel);
        PlayerListPanel playerListPanel = new PlayerListPanel(this);
        propertyChangeListenerList.add(chatPanel);
        propertyChangeListenerList.add(playerListPanel);
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                playerListPanel, chatPanel);
        splitPane.setOneTouchExpandable(true);
        splitPane.setResizeWeight(0.5);

        JPanel leftPanel = new JPanel(new GridLayout(1,0));
        leftPanel.setPreferredSize(new Dimension(300,10));
        leftPanel.add(splitPane);
        return leftPanel;
    }

    public void addChatListener(ChatListener chatListener){
        synchronized (chatListenerList) {
            chatListenerList.add(chatListener);
        }
    }

    private void RunNicknameScene(){
        sceneManager.setScene(SceneID.getNicknameSelectSceneID());
    }

    @Override
    public void run() throws RemoteException, TimeoutException, DisconnectException {
        SwingUtilities.invokeLater(
                this::RunNicknameScene
        );
        SwingUtilities.invokeLater(
                this::createGUI
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
                    this.dispose();
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

    public void notifyServerFailure() {
        synchronized (inputQueue) {
            inputQueue.add("SERVER_FAILURE");
            inputQueue.notifyAll();
        }
    }

    public void addSubject(String nickname) {
        ViewHand playerHand = inputHandler.getPlayerHand(nickname);
        observableComponents.add(playerHand);
    }
}
