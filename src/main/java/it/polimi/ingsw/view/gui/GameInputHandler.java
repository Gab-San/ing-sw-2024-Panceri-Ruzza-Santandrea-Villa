package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.GamePhase;
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

public class GameInputHandler{
    private final CommandPassthrough serverProxy;
    private final GameGUI gui;
    private final ViewController controller;
    private final ExecutorService threadPool;
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

    public void showError(String errorMsg){
        //threaded to prevent stalling while displaying
        threadPool.submit(() -> gui.showError(errorMsg));
    }

    public void sendMsg(String addressee, String message) throws RemoteException {
        serverProxy.sendMsg(addressee, message);
    }

    public void connect(String nickname) throws RemoteException, IllegalStateException {
        if (!validateNickname(nickname)) {
            throw new IllegalStateException(UIFunctions.evaluateErrorType(nickname));
        }
        controller.addLocalPlayer(nickname);
        controller.setSelfPlayerArea();
        serverProxy.connect(nickname);
    }

    public void setNumOfPlayers(int numOfPlayers) throws RemoteException {
        serverProxy.setNumOfPlayers(numOfPlayers);
    }


    public void disconnect() throws IllegalStateException, IllegalArgumentException, RemoteException {

    }


    public void placeStartCard(boolean placeOnFront) throws RemoteException, IllegalStateException {
        controller.validatePlaceStartCard();
        serverProxy.placeStartCard(placeOnFront);
    }


    public void chooseColor(char colour) throws RemoteException {
        serverProxy.chooseColor(colour);
    }


    public void chooseObjective(ViewObjectiveCard chosenCard) throws RemoteException, IllegalStateException {
        if(chosenCard == null){
            throw new IllegalStateException("Choose a card!");
        }
        int choice = controller.getObjectiveCardIndex(chosenCard);
        serverProxy.chooseObjective(choice);
    }


    public void placeCard(String cardID, GamePoint placePos, String cornerDir, boolean placeOnFront) throws RemoteException, IllegalStateException {
        controller.validatePlaceCard(cardID, placePos, cornerDir);
        serverProxy.placeCard(cardID, placePos, cornerDir, placeOnFront);
    }


    public void draw(char deck, int cardPosition) throws RemoteException, IllegalStateException {
        controller.validateDraw(deck,cardPosition);
        serverProxy.draw(deck, cardPosition);
    }


    public void restartGame(int numOfPlayers) throws RemoteException {

    }

    public void notifyDisconnection(){
        // Calls to actions block the UI thread, so all the actions
        // should be called on threads unless a synchronous response is wanted.
        // In this case, this action would block the thread as it waits for the
        // inputQueue to update and parse the notification
        threadPool.execute( gui::notifyServerFailure );
    }

    public ViewHand getPlayerHand(String nickname) {
        return controller.getPlayer(nickname);
    }

    public ViewPlayArea getPlayArea(String nickname) { return controller.getPlayArea(nickname);}

    public void addChatListener(ChatListener chatListener) {
        gui.addChatListener(chatListener);
    }

    public void addPropertyListener(PropertyChangeListener pcl) {
        gui.addToPropListeners(pcl);
    }

    /**
     * Invokes the scene identified by the unique sceneId to be displayed next.
     * @param nextSceneID next scene identifier
     */
    public void changeScene(SceneID nextSceneID) {
        GUI_Scene nextScene = (GUI_Scene) SceneManager.getInstance().getScene(nextSceneID);
        gui.changeScene(nextScene);
    }

    public boolean isLocalPlayer(String nickname) {
        return controller.isLocalPlayer(nickname);
    }

    public ViewHand getLocalPlayer() {
        return controller.getLocalPlayer();
    }
}
