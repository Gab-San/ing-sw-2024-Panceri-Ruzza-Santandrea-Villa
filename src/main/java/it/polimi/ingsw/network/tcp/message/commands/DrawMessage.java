package it.polimi.ingsw.network.tcp.message.commands;

import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.VirtualServer;
import it.polimi.ingsw.network.tcp.message.TCPClientMessage;

import java.io.Serial;
import java.rmi.RemoteException;

/**
 * This class implements tcp client message interface. Sent if a draw action is requested.
 */
public class DrawMessage implements TCPClientMessage {

    @Serial
    private static final long serialVersionUID = 65L;
    private final String nickname;
    private final char deck;
    private final int card;

    /**
     * Constructs the draw message.
     * @param nickname unique id of the user
     * @param deck identifier of the deck to modify
     * @param cardPosition deck position to modify
     */
    public DrawMessage(String nickname, char deck, int cardPosition) {
        this.nickname = nickname;
        this.deck = deck;
        this.card = cardPosition;
    }


    @Override
    public void execute(VirtualServer virtualServer, VirtualClient virtualClient) throws RemoteException {
        virtualServer.draw(nickname, virtualClient, deck, card);
    }

    @Override
    public boolean isCheck() {
        return false;
    }
}
