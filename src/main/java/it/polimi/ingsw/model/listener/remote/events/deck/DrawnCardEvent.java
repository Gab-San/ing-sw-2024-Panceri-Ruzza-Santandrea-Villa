package it.polimi.ingsw.model.listener.remote.events.deck;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.deck.PlayableDeck;
import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

public class DrawnCardEvent extends DeckEvent{
    private final Card topCard;
    public DrawnCardEvent(char deck, Card topCard) {
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

    @Override
    public String toString() {
        return super.toString();
    }
}
