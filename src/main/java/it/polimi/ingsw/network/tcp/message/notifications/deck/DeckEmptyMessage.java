package it.polimi.ingsw.network.tcp.message.notifications.deck;

import it.polimi.ingsw.model.deck.PlayableDeck;
import it.polimi.ingsw.network.VirtualClient;

import java.io.Serial;
import java.rmi.RemoteException;

public class DeckEmptyMessage extends DeckMessage{
    @Serial
    private static final long serialVersionUID = 51293871093L;
    private final int cardPos;
    public DeckEmptyMessage(char deck, int cardPos) {
        super(deck);
        this.cardPos = cardPos;
    }

    public DeckEmptyMessage(char deck){
        super(deck);
        this.cardPos = PlayableDeck.TOP_POSITION;
    }

    @Override
    public void execute(VirtualClient virtualClient) throws RemoteException {

        if(cardPos == PlayableDeck.TOP_POSITION){
            virtualClient.emptyFaceDownPile(deck);
            return;
        }

        virtualClient.emptyReveal(deck, cardPos);
    }
}
