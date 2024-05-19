package it.polimi.ingsw.model.listener.remote.events.deck;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.deck.PlayableDeck;
import it.polimi.ingsw.server.VirtualClient;

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
        if(topId == null && firstId == null && secondId == null){
            virtualClient.createEmptyDeck(deck);
            return;
        }

        if(topId == null && firstId == null){
            virtualClient.setDeckState(deck, secondId, PlayableDeck.SECOND_POSITION);
            return;
        }
        if(topId == null && secondId == null){
            virtualClient.setDeckState(deck ,firstId, PlayableDeck.FIRST_POSITION);
        }
        if(topId == null){
            virtualClient.setDeckState(deck, firstId, secondId);
        }
        virtualClient.setDeckState(deck, topId, firstId, secondId);
    }
}
