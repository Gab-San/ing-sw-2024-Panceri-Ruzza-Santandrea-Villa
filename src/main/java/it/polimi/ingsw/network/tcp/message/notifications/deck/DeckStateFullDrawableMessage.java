package it.polimi.ingsw.network.tcp.message.notifications.deck;

import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

public class DeckStateFullDrawableMessage extends DeckMessage {
    private static final long serialVersionUID = 1545189723L;
    private final String topID, firstRevealedID, secondRevealedID;
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
