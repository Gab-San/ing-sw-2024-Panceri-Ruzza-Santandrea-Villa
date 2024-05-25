package it.polimi.ingsw.network.tcp.message.notifications.deck;

import it.polimi.ingsw.network.VirtualClient;

import java.io.Serial;
import java.rmi.RemoteException;

public class DeckStateOnlyRevealedMessage extends DeckMessage{
    @Serial
    private static final long serialVersionUID = 561829389L;
    private final String firstRevID, secondRevID;
    public DeckStateOnlyRevealedMessage(char deck, String firstRevID, String secondRevID) {
        super(deck);
        this.firstRevID = firstRevID;
        this.secondRevID = secondRevID;
    }

    @Override
    public void execute(VirtualClient virtualClient) throws RemoteException {
        virtualClient.setDeckState(deck, firstRevID, secondRevID);
    }
}
