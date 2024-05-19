package it.polimi.ingsw.listener.remote.events.stub;


import com.diogonunes.jcolor.Attribute;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.PlayerColor;
import it.polimi.ingsw.server.VirtualClient;

import java.rmi.RemoteException;

import static com.diogonunes.jcolor.Ansi.colorize;

public class PuppetClient implements VirtualClient {
    private final StubView view;
    private final String nickname;
    public boolean notificationReceived;

    public PuppetClient(String nickname, StubView view) {
        this.view = view;
        this.nickname = nickname;
    }

    @Override
    public void update(String msg) throws RemoteException {

    }

    @Override
    public void ping() throws RemoteException {

    }


    @Override
    public synchronized void updatePlayer(String nickname, PlayerColor colour) throws RemoteException {
        view.getPlayer(nickname).setColor(colour);
    }

    @Override
    public synchronized void updatePlayer(String nickname, int playerTurn) throws RemoteException {
        System.out.println(colorize("Being notified by " + this.nickname + "\nof player creation with:\n" +
                nickname + "\n" +
                playerTurn, Attribute.MAGENTA_TEXT()));
        view.getPlayer(nickname).setTurn(playerTurn);
    }

    @Override
    public synchronized void updatePlayer(String nickname, boolean isConnected) throws RemoteException {
        view.getPlayer(nickname).setConnected(isConnected);
    }

    @Override
    public void createPlayer(String nickname, boolean isConnected, int turn, PlayerColor colour) throws RemoteException {
        System.out.println(colorize("Being notified by " + this.nickname + "\nof player creation with:\n" +
                nickname + "\n" +
                isConnected + "\n" +
                turn + "\n" +
                colour, Attribute.MAGENTA_TEXT()));
        notificationReceived = true;
        view.addPlayer(nickname, isConnected, turn, colour);
    }

    @Override
    public void deckReveal(char deck, String revealedId, int cardPosition) throws RemoteException {

    }

    @Override
    public void createDeck(char deck, String topId, String firstId, String secondId) throws RemoteException {
        System.out.println(colorize("Being notified by " + this.nickname + "\nof deck creation with:\n" +
                "[DECK TYPE]" + deck + "\n" +
                "[TOP CARD]" + topId + "\n" +
                "[FIRST CARD]" + firstId + "\n" +
                "[SECOND CARD]" + secondId, Attribute.MAGENTA_TEXT()));
        notificationReceived = true;
    }

    @Override
    public void deckUpdate(char deck, String cardID) throws RemoteException {

    }

    @Override
    public void emptyDeck(char deck) throws RemoteException {

    }

    @Override
    public void updatePhase(GamePhase gamePhase) throws RemoteException {

    }

    @Override
    public void updateScore(String nickname, int score) throws RemoteException {

    }

    @Override
    public void updateTurn(int currentTurn) throws RemoteException {

    }

}
