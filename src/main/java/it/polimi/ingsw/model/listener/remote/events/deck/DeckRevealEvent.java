package it.polimi.ingsw.model.listener.remote.events.deck;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

/**
 * This class represents a deck event. It is triggered by a card being revealed.
 */
public class DeckRevealEvent extends DeckEvent{
    private final int cardPosition;
    private final Card revealedCard;

    /**
     * Constructs the deck reveal event.
     * @param deck deck type id
     * @param revealedCard revealed card
     * @param cardPosition revealed card's position in the deck
     */
    public DeckRevealEvent(char deck, Card revealedCard, int cardPosition) {
        super(deck);
        this.revealedCard = revealedCard;
        this.cardPosition = cardPosition;
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        String revealedId = (revealedCard == null) ? null : revealedCard.getCardID();
        if(revealedId == null){
            virtualClient.emptyReveal(deck, cardPosition);
            return;
        }
        virtualClient.deckUpdate(deck, revealedId, cardPosition);
    }

}
