package it.polimi.ingsw.network.tcp.message.notifications.deck;

import it.polimi.ingsw.network.VirtualClient;

import java.io.Serial;
import java.rmi.RemoteException;

/**
 * This class represents a deck message. It carries information about initialization of a not full deck.
 */
public class DeckStateMessage extends DeckMessage{
    @Serial
    private static final long serialVersionUID = 1589065897238L;
    private final String revealedID;
    private final int cardPos;

    /**
     * Constructs the deck state message for a not empty deck.
     * @param deck deck unique identifier
     * @param revealedID identifier of the revealed card
     * @param cardPos position in which the card was revealed
     */
    public DeckStateMessage(char deck, String revealedID, int cardPos) {
        super(deck);
        this.revealedID = revealedID;
        this.cardPos = cardPos;
    }

    /**
     * Constructs the deck state message for an empty deck.
     * @param deck deck unique identifier
     */
    public DeckStateMessage(char deck){
        super(deck);
        revealedID = null;
        cardPos = -1;
    }

    @Override
    public void execute(VirtualClient virtualClient) throws RemoteException {
        if(revealedID == null){
            virtualClient.setEmptyDeckState(deck);
            return;
        }

        virtualClient.setDeckState(deck, revealedID, cardPos);
    }
}
