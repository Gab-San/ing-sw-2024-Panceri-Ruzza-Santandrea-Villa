package it.polimi.ingsw.model.listener.remote.events.deck;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.deck.PlayableDeck;
import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

public class DeckStateUpdateEvent extends DeckEvent{
    private final Card topCard;
    private final Card firstRevealedCard;
    private final Card secondRevealedCard;

    public DeckStateUpdateEvent(char deck, Card topCard, Card firstRevealedCard, Card secondRevealedCard) {
        super(deck);
        this.topCard = topCard;
        this.firstRevealedCard = firstRevealedCard;
        this.secondRevealedCard = secondRevealedCard;
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        String topId = (topCard == null) ? null : topCard.getCardID();
        String firstId = (firstRevealedCard == null) ? null : firstRevealedCard.getCardID();
        String secondId = (secondRevealedCard == null) ? null : secondRevealedCard.getCardID();
        // Deck is empty
        if(topId == null && firstId == null && secondId == null){
            virtualClient.setEmptyDeckState(deck);
            return;
        }
        // Deck has only second revealed card
        if(topId == null && firstId == null){
            virtualClient.setDeckState(deck, secondId, PlayableDeck.SECOND_POSITION);
            return;
        }
        // Deck has only first revealed card
        if(topId == null && secondId == null){
            virtualClient.setDeckState(deck ,firstId, PlayableDeck.FIRST_POSITION);
            return;
        }
        // Deck face-down pile has depleted. In fact, it can't exist a configuration
        // in which the face-down pile is not empty and there is an empty revealed card
        // because a card is revealed whenever a revealed card is drawn.
        if(topId == null){
            virtualClient.setDeckState(deck, firstId, secondId);
            return;
        }
        // There are at least three cards accounting for a full deck.
        virtualClient.setDeckState(deck, topId, firstId, secondId);
    }


}
