package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.network.CommandPassthrough;
import it.polimi.ingsw.view.*;
import it.polimi.ingsw.view.events.DisplayEvent;
import it.polimi.ingsw.view.events.GUIEvent;
import it.polimi.ingsw.view.exceptions.DisconnectException;
import it.polimi.ingsw.view.exceptions.TimeoutException;
import it.polimi.ingsw.view.gui.localarea.PlayerArea;
import it.polimi.ingsw.view.gui.scenes.connection.ConnectionScene;
import it.polimi.ingsw.view.gui.scenes.game.BoardScene;
import it.polimi.ingsw.view.model.ViewBoard;

import javax.swing.*;
import java.rmi.RemoteException;
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
    public GUI(CommandPassthrough serverProxy, Consumer<ModelUpdater> setClientModelUpd, BlockingQueue<String> inputQueue){

        this.inputQueue = inputQueue;
        ViewBoard board = new ViewBoard(this);
        ModelUpdater modelUpdater = new ModelUpdater(board);
        setClientModelUpd.accept(modelUpdater);
        inputHandler = new GameInputHandler(serverProxy, this, new ViewController(board));

        SceneManager.getInstance().loadScene(SceneID.getNicknameSelectSceneID(), new ConnectionScene(inputHandler));
    }

    @Override
    public synchronized void update(SceneID sceneID, DisplayEvent event) {
        Scene scene = SceneManager.getInstance().getScene(sceneID);
        if (!(event instanceof GUIEvent guiEvent)) {
            return;
        }
        guiEvent.displayEvent(this);
    }


    public synchronized void updatePhase(GamePhase gamePhase){
//        switch (gamePhase){
//            case SETNUMPLAYERS ->
//                    map.putIfAbsent(SceneID.getSetNumberOfPlayersID(), SetPlayersScene::new);
//            case JOIN, SETUP, PLACESTARTING,
//                    CHOOSECOLOR, DEALCARDS,
//                    CHOOSEOBJECTIVE, CHOOSEFIRSTPLAYER,
//                    PLACECARD, DRAWCARD->
//                    map.putIfAbsent(SceneID.getMyAreaSceneID(), GameScene::new);
//            case EVALOBJ, SHOWWIN ->
//                    map.putIfAbsent(SceneID.getEndgameSceneID(), EndgameScene::new);
//        }
    }

    private synchronized void displayNextScene(GUI_Scene nextScene) {
        assert nextScene instanceof JPanel;
        GUI_Scene currentScene = (GUI_Scene) SceneManager.getInstance().getCurrentScene();
        currentScene.close();
        SceneManager.getInstance().remove(currentScene);
        add((JPanel) nextScene);
        SceneManager.getInstance().setScene(nextScene);
        this.pack();
        this.setVisible(true);
    }

    @Override
    public synchronized void showError(String errorMsg) {

    }

    @Override
    public synchronized void showChatMessage(String msg) {

    }

    @Override
    public synchronized void notifyTimeout() {

    }
    private void createGUI(){
//        // Standard JFrame stuff defining window size, position and layout
//        this.setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
//        Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
//        center.translate(-SCREEN_WIDTH/2, -SCREEN_HEIGHT/2);
//        this.setLocation(center);
//        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
//        this.setLayout(new BorderLayout());
    }
    private void RunNicknameScene(){
        SceneManager.getInstance().setScene(SceneID.getNicknameSelectSceneID());
    }

    @Override
    public void run() throws RemoteException, TimeoutException, DisconnectException {
        SwingUtilities.invokeLater(
                this::RunNicknameScene
        );
        GUI_Scene nextScene = new PlayerArea();
        SceneManager.getInstance().loadScene(SceneID.getMyAreaSceneID(),
                nextScene);
        final int SLEEP_MILLIS = 200;
        try {
            while (true) {
                inputQueue.poll(SLEEP_MILLIS, TimeUnit.MILLISECONDS);
            }
        } catch (InterruptedException interruptedException){
            interruptedException.printStackTrace(System.err);
        } catch (IllegalArgumentException | IllegalStateException e){
            showError(e.getMessage());
        }
    }
}
