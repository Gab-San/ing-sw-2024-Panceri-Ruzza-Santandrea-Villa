package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.network.CommandPassthrough;
import it.polimi.ingsw.view.Client;
import it.polimi.ingsw.view.UIFunctions;
import it.polimi.ingsw.view.ViewController;
import it.polimi.ingsw.view.model.ViewHand;

import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameInputHandler implements CommandPassthrough{
    private final CommandPassthrough serverProxy;
    private final GUI gui;
    private final ViewController controller;
    private final ExecutorService threadPool;
    public GameInputHandler(CommandPassthrough serverProxy, GUI gui, ViewController controller){
        this.serverProxy = serverProxy;
        this.gui = gui;
        this.controller = controller;
        threadPool = Executors.newCachedThreadPool();
    }

    private boolean validateNickname(String nickname){
        return nickname.matches("[^\n ].*[a-zA-Z].*[^\n ]")
                && nickname.length() < Client.MAX_NICKNAME_LENGTH;
    }

    @Override
    public void sendMsg(String addressee, String message) throws RemoteException {

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

    @Override
    public void disconnect() throws IllegalStateException, IllegalArgumentException, RemoteException {

    }

    @Override
    public void placeStartCard(boolean placeOnFront) throws RemoteException {
        serverProxy.placeStartCard(placeOnFront);
    }

    @Override
    public void chooseColor(char colour) throws RemoteException {
        serverProxy.chooseColor(colour);
    }

    @Override
    public void chooseObjective(int choice) throws RemoteException {

    }

    @Override
    public void placeCard(String cardID, GamePoint placePos, String cornerDir, boolean placeOnFront) throws RemoteException {

    }

    @Override
    public void draw(char deck, int cardPosition) throws RemoteException {

    }

    @Override
    public void restartGame(int numOfPlayers) throws RemoteException {

    }

    @Override
    public void ping() throws RemoteException {

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
}
