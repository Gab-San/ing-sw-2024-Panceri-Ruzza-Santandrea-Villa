package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.network.CommandPassthrough;
import it.polimi.ingsw.view.*;
import it.polimi.ingsw.view.model.ViewHand;
import it.polimi.ingsw.view.model.ViewPlayArea;
import it.polimi.ingsw.view.model.cards.ViewObjectiveCard;

import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class acts as the handler for all the user inputs that either modify
 * the gui or are sent to the server.
 */
public class GameInputHandler{
    private final CommandPassthrough serverProxy;
    private final GameGUI gui;
    private final ViewController controller;
    private final ExecutorService threadPool;

    /**
     * Constructs the game input handler.
     * @param serverProxy enter point of the network connection
     * @param gui gui on which this input handler is attached to
     * @param controller view model controller
     */
    public GameInputHandler(CommandPassthrough serverProxy, GameGUI gui, ViewController controller){
        this.serverProxy = serverProxy;
        this.gui = gui;
        this.controller = controller;
        threadPool = Executors.newCachedThreadPool();
    }

    private boolean validateNickname(String nickname){
        return nickname.matches("[^\n ].*[a-zA-Z].*[^\n ]")
                && nickname.length() < Client.MAX_NICKNAME_LENGTH;
    }

    /**
     * Requests an error to be shown on the gui.
     * @param errorMsg error message text
     */
    public synchronized void showError(String errorMsg){
        //threaded to prevent stalling while displaying
        threadPool.submit(() -> gui.showError(errorMsg));
    }

    /**
     * Sends a message to the selected addressee.
     * @param addressee message receiver
     * @param message message body
     * @throws RemoteException if an error occurs during connection
     */
    public synchronized void sendMsg(String addressee, String message) throws RemoteException {
        serverProxy.sendMsg(addressee, message);
    }

    /**
     * Attempts to connect the user to the server with the chosen nickname.
     * @param nickname user identifier
     * @throws RemoteException if an error occurs during connection
     * @throws IllegalStateException if the nickname format isn't valid
     */
    public synchronized void connect(String nickname) throws RemoteException, IllegalStateException {
        if (!validateNickname(nickname)) {
            throw new IllegalStateException(UIFunctions.evaluateErrorType(nickname));
        }
        controller.addLocalPlayer(nickname);
        controller.setSelfPlayerArea();
        serverProxy.connect(nickname);
    }

    /**
     * Attempts to set the number of players.
     * @param numOfPlayers number of players to play the match.
     * @throws RemoteException if a connection error occurs
     */
    public synchronized void setNumOfPlayers(int numOfPlayers) throws RemoteException {
        serverProxy.setNumOfPlayers(numOfPlayers);
    }

    /**
     * Attempts to disconnect the player from the lobby.
     * @throws IllegalStateException
     * @throws IllegalArgumentException
     * @throws RemoteException
     */
    public synchronized void disconnect() throws IllegalStateException, IllegalArgumentException, RemoteException {

    }

    /**
     * Attempts to place the starting card.
     * @param placeOnFront true if the starting card is facing up, false otherwise
     * @throws IllegalStateException if the starting card cannot be placed at the time of the invocation
     */
    public synchronized void placeStartCard(boolean placeOnFront) throws IllegalStateException {
        controller.validatePlaceStartCard();
//        threadPool.submit(() -> {
                    try {
                        serverProxy.placeStartCard(placeOnFront);
                    } catch (RemoteException e) {
                        showError("CONNECTION LOST.");
                        notifyDisconnection();
                    }
//                }
//        );
    }

    /**
     * Attempts to choose color of the player.
     * @param colour chosen color
     * @throws RemoteException if a connection error occurs
     */
    public synchronized void chooseColor(char colour) throws RemoteException {
        serverProxy.chooseColor(colour);
    }

    /**
     * Attempts to choose secret objective card.
     * @param chosenCard chosen objective card
     * @throws RemoteException if a connection error occurs
     * @throws IllegalStateException if no card is selected
     */
    public synchronized void chooseObjective(ViewObjectiveCard chosenCard) throws RemoteException, IllegalStateException {
        if(chosenCard == null){
            throw new IllegalStateException("Choose a card!");
        }
        int choice = controller.getObjectiveCardIndex(chosenCard);
        serverProxy.chooseObjective(choice);
    }

    /**
     * Attempts to place a card.
     * @param cardID placing card id
     * @param placePos position on which the card has to be placed
     * @param cornerDir corner direction
     * @param placeOnFront true if the card is facing up, false otherwise
     * @throws IllegalStateException if the card cannot be placed at the time of the invocation
     */
    public synchronized void placeCard(String cardID, GamePoint placePos, String cornerDir, boolean placeOnFront) throws IllegalStateException {
        controller.validatePlaceCard(cardID, placePos, cornerDir);
//        threadPool.submit(() -> {
                    try {
                        serverProxy.placeCard(cardID, placePos, cornerDir, placeOnFront);
                    } catch (RemoteException e) {
                        showError("CONNECTION LOST.");
                        notifyDisconnection();
                    }
//                }
//        );

    }


    public synchronized void draw(char deck, int cardPosition) throws RemoteException, IllegalStateException {
        controller.validateDraw(deck,cardPosition);
        serverProxy.draw(deck, cardPosition);
    }


    public synchronized void restartGame(int numOfPlayers) throws IllegalArgumentException {
        try{
            controller.validateRestart(numOfPlayers);
        }catch (IllegalArgumentException e){
            showError(e.getMessage());
            throw e;
        }
//        threadPool.submit(() -> {
                    try {
                        serverProxy.restartGame(numOfPlayers);
                    } catch (RemoteException e) {
                        showError("CONNECTION LOST.");
                        notifyDisconnection();
                    }
//                }
//        );
    }

    public synchronized void notifyDisconnection(){
        // Calls to actions block the UI thread, so all the actions
        // should be called on threads unless a synchronous response is wanted.
        // In this case, this action would block the thread as it waits for the
        // inputQueue to update and parse the notification
        threadPool.execute( gui::notifyServerFailure );
    }

    public synchronized ViewHand getPlayerHand(String nickname) {
        return controller.getPlayer(nickname);
    }

    public synchronized ViewPlayArea getPlayArea(String nickname) { return controller.getPlayArea(nickname);}

    public synchronized void addChatListener(ChatListener chatListener) {
        gui.addChatListener(chatListener);
    }

    public synchronized void addPropertyListener(PropertyChangeListener pcl) {
        gui.addToPropListeners(pcl);
    }

    /**
     * Invokes the scene identified by the unique sceneId to be displayed next.
     * @param nextSceneID next scene identifier
     */
    public synchronized void changeScene(SceneID nextSceneID) {
        GUI_Scene nextScene = (GUI_Scene) SceneManager.getInstance().getScene(nextSceneID);
        if(nextScene == null){
            throw new IllegalArgumentException("Scene wasn't loaded");
        }
        gui.changeScene(nextScene);
    }

    public synchronized boolean isLocalPlayer(String nickname) {
        return controller.isLocalPlayer(nickname);
    }

    public synchronized ViewHand getLocalPlayer() {
        return controller.getLocalPlayer();
    }
}
