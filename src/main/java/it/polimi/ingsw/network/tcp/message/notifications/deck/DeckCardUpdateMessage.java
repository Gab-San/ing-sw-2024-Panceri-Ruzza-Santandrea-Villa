package it.polimi.ingsw.network.tcp.message.notifications.deck;

import it.polimi.ingsw.network.VirtualClient;

import java.io.Serial;
import java.rmi.RemoteException;

/**
 * This class represents a deck message. It is sent when a card is drawn from the deck.
 */
public class DeckCardUpdateMessage extends DeckMessage{
    @Serial
    private static final long serialVersionUID = 165192786381741L;
    private final String cardID;
    private final int cardPos;

    /**
     * Constructs the deck update message.
     * @param deck identifier of the deck
     * @param cardID identifier of the card
     * @param cardPos position on the deck from which the card was drawn
     */
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
