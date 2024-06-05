package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.network.CommandPassthrough;
import it.polimi.ingsw.view.*;
import it.polimi.ingsw.view.gui.scenes.connection.ConnectionScene;
import it.polimi.ingsw.view.gui.scenes.board.BoardScene;
import it.polimi.ingsw.view.model.ViewBoard;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

public class GUI extends View {
    private final CommandPassthrough serverProxy;
    private final BlockingQueue<String> inputQueue;
    private GameWindow window;
    private ViewBoard board;
    private InputHandler inputHandler;
    private final Object DISCONNECTION_LOCK = new Object();
    private boolean cannotConnect;

    public GUI(CommandPassthrough serverProxy, Consumer<ModelUpdater> setClientModelUpdater, BlockingQueue<String> inputQueue){
        super(new ConnectionScene()); // change this scene to GUI scene
        synchronized (DISCONNECTION_LOCK) {
            cannotConnect = false;
        }

        SwingUtilities.invokeLater(
                () -> {
                    try {
                        createGUIandRunNicknameScene(setClientModelUpdater);
                    } catch (RemoteException e) {
                        synchronized (DISCONNECTION_LOCK) {
                            cannotConnect = true;
                        }
                    }
                }
        );
        this.serverProxy = serverProxy;
        sceneIDMap.put(SceneID.getNicknameSelectSceneID(), currentScene);
        this.inputQueue = inputQueue;
        // connection has concluded succesfully
    }

    private void createGUIandRunNicknameScene(Consumer<ModelUpdater> setClientModelUpdater) throws RemoteException {
        window = new GameWindow();
        runNicknameScene(setClientModelUpdater);
    }

    @Override
    public void setScene(SceneID sceneID) throws IllegalArgumentException {
        window.hideScene(currentScene);
        System.out.println("Transitioning from " + currentScene +
                " to scene " + sceneID);
        currentScene = sceneIDMap.get(sceneID);
        window.displayScene(currentScene);
    }

    @Override
    public synchronized void update(SceneID sceneID, String description) {
        Scene scene = sceneIDMap.get(sceneID);
        // If the scene was already in the scenes map
        if(scene != null){
            // If the scene involved in the update is the current scene
            if(currentScene.equals(scene)){
                System.out.println("Updating scene");
                SwingUtilities.invokeLater(
                        () ->currentScene.display()
                );
            }
        }
    }

    @Override
    public synchronized void showError(String errorMsg) {

    }

    @Override
    public synchronized void showNotification(String notification) {

    }

    @Override
    public synchronized void showChatMessage(String msg) {

    }

    @Override
    public synchronized void notifyTimeout() {

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

    // Nickname choice and connection
    private void runNicknameScene(Consumer<ModelUpdater> setClientModelUpdater) throws RemoteException{
        setScene(SceneID.getNicknameSelectSceneID());
        String nickname = "";
        do {
            String nicknameSelectorScene = JOptionPane.showInputDialog(
                    (Component) currentScene,
                    "Insert user name...", "Nickname selection",
                    JOptionPane.PLAIN_MESSAGE
            );
            if(validateNickname(nicknameSelectorScene)) {
                try {
                    // Tries to create a board with the current player nickname
                    synchronized (this) {
                        board = new ViewBoard(nicknameSelectorScene);
                    }
                    // Effectively creates the input handler
                    inputHandler = new InputHandler(serverProxy, new ViewController(board));
                    setClientModelUpdater.accept(new ModelUpdater(board, this));
                    // Tries to connect
                    inputHandler.connect(nicknameSelectorScene);
                    // Statement not reached if connection has failed
                    nickname = nicknameSelectorScene;
                } catch (IllegalStateException e){
                    currentScene.displayError("Join failed. Server can't accommodate you now\n" + e.getMessage());
                } catch (NullPointerException exception){
                    //In this case the player refused to connect
                    System.exit(-1);
                }
            } else {
                // Wrong nickname format
                String error = UIFunctions.evaluateErrorType(nickname);
                currentScene.displayError(error);
            }
        }while (!validateNickname(nickname));
        synchronized (this){
            this.notifyAll();
        }
        // The nickname scene is not needed
        window.closeCurrentScene( currentScene );
    }

    @Override
    public void run() throws RemoteException {
        synchronized (DISCONNECTION_LOCK){
            if(cannotConnect) throw new RemoteException();
        }
        synchronized (this){
            while(board == null){
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        sceneIDMap.put(SceneID.getBoardSceneID(), new BoardScene(this));
        setScene(SceneID.getBoardSceneID());
        try {
            inputQueue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public ViewBoard getBoard(){
        return board;
    }
}