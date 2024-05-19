package it.polimi.ingsw.model.listener.remote.events.deck;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.deck.PlayableDeck;
import it.polimi.ingsw.server.VirtualClient;

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
            virtualClient.emptyDeck(deck);
            return;
        }
        virtualClient.deckUpdate(deck, topCard.getCardID(), PlayableDeck.TOP_POSITION);
    }
}
