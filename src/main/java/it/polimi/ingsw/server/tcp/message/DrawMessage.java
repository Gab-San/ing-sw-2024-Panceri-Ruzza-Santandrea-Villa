package it.polimi.ingsw.server.tcp.message;

import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.VirtualServer;

import java.io.Serial;
import java.rmi.RemoteException;

public class DrawMessage implements TCPClientMessage{

    @Serial
    private static final long serialVersionUID = 65L;
    private final String nickname;
    private final char deck;
    private final int card;

    public DrawMessage(String nickname, char deck, int card) {
        this.nickname = nickname;
        this.deck = deck;
        this.card = card;
    }


    @Override
    public void execute(VirtualServer virtualServer, VirtualClient virtualClient) throws RemoteException {
        virtualServer.draw(nickname, virtualClient, deck, card);
    }
}
