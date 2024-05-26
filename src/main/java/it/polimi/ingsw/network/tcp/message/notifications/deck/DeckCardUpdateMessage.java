package it.polimi.ingsw.network.tcp.message.notifications.deck;

import it.polimi.ingsw.network.VirtualClient;

import java.io.Serial;
import java.rmi.RemoteException;

public class DeckCardUpdateMessage extends DeckMessage{
    @Serial
    private static final long serialVersionUID = 165192786381741L;
    private final String cardID;
    private final int cardPos;
    public DeckCardUpdateMessage(char deck, String cardID, int cardPos) {
        super(deck);
        this.cardID = cardID;
        this.cardPos = cardPos;
    }

    @Override
    public void execute(VirtualClient virtualClient) throws RemoteException {
        virtualClient.deckUpdate(deck, cardID, cardPos);
    }
}
