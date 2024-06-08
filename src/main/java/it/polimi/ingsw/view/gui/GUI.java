package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.network.CommandPassthrough;
import it.polimi.ingsw.view.*;
import it.polimi.ingsw.view.events.DisplayEvent;
import it.polimi.ingsw.view.exceptions.DisconnectException;
import it.polimi.ingsw.view.exceptions.TimeoutException;
import it.polimi.ingsw.view.gui.scenes.connection.ConnectionScene;

import javax.swing.*;
import java.awt.*;
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
        inputHandler = new GameInputHandler(serverProxy, this);
        SceneManager.getInstance().loadScene(SceneID.getNicknameSelectSceneID(), new ConnectionScene(inputHandler));

        // Effective GUI setup
        SwingUtilities.invokeLater(
                this::createGUIandRunNicknameScene
        );
    }



    private void createGUIandRunNicknameScene(){
//        // Standard JFrame stuff defining window size, position and layout
//        this.setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
//        Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
//        center.translate(-SCREEN_WIDTH/2, -SCREEN_HEIGHT/2);
//        this.setLocation(center);
//        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
//        this.setLayout(new BorderLayout());ù

        SceneManager.getInstance().setScene(SceneID.getNicknameSelectSceneID());
    }

    @Override
    public void update(SceneID sceneID, DisplayEvent event) {

    }

    @Override
    public void showError(String errorMsg) {

    }

    @Override
    public void showChatMessage(String msg) {

    }

    @Override
    public void notifyTimeout() {

    }

    @Override
    public void run() throws RemoteException, TimeoutException, DisconnectException {

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
