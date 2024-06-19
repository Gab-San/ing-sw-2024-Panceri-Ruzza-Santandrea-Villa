package it.polimi.ingsw.model.listener.remote.events.deck;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.deck.PlayableDeck;
import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

/**
 * This class represents a deck event, triggered by a card being drawn and the top card being replaced.
 */
public class FaceDownReplaceEvent extends DeckEvent{
    private final Card topCard;

    /**
     * Constructs a replace top card event.
     * @param deck deck type id
     * @param topCard updated face-down pile visible top card
     */
    public FaceDownReplaceEvent(char deck, Card topCard) {
        super(deck);
        this.topCard = topCard;
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        if(topCard == null){
            virtualClient.emptyFaceDownPile(deck);
            return;
        }

        virtualClient.deckUpdate(deck, topCard.getCardID(), PlayableDeck.TOP_POSITION);
    }
}
