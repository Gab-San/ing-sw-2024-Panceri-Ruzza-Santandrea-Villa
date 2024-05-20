package it.polimi.ingsw.model.listener.remote.events.deck;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

public class DeckRevealEvent extends DeckEvent{
    private final int cardPosition;
    private final Card revealedCard;

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
