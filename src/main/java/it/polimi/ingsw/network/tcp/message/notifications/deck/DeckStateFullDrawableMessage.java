package it.polimi.ingsw.network.tcp.message.notifications.deck;

import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

/**
 * This class represents a deck message that notifies of a deck
 * that contains at least three cards at initialization, one for each position.
 */
public class DeckStateFullDrawableMessage extends DeckMessage {
    private static final long serialVersionUID = 1545189723L;
    private final String topID, firstRevealedID, secondRevealedID;

    /**
     * Constructs the full deck message.
     * @param deck unique identifier of the deck
     * @param topID identifier of the card placed on top of the face-down pile
     * @param firstRevealedID identifier of the first revealed card
     * @param secondRevealedID identifier of the second revealed card
     */
    public DeckStateFullDrawableMessage(char deck, String topID, String firstRevealedID, String secondRevealedID) {
        super(deck);
        this.topID = topID;
        this.firstRevealedID = firstRevealedID;
        this.secondRevealedID = secondRevealedID;
    }

    @Override
    public void execute(VirtualClient virtualClient) throws RemoteException {
        virtualClient.setDeckState(deck, topID, firstRevealedID, secondRevealedID);
    }
}
