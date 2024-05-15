package it.polimi.ingsw.server.tcp.client.message;

import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.VirtualServer;
import it.polimi.ingsw.server.tcp.message.TCPClientMessage;

import java.io.Serial;
import java.rmi.RemoteException;

public class PlaceStartingCardMessage implements TCPClientMessage {
    @Serial
    private static final long serialVersionUID = 8888L;
    private final String nickname;
    private final boolean placeOnFront;

    public PlaceStartingCardMessage(String nickname, boolean placeOnFront) {
        this.nickname = nickname;
        this.placeOnFront = placeOnFront;
    }


    @Override
    public void execute(VirtualServer virtualServer, VirtualClient virtualClient) throws RemoteException {
        virtualServer.placeStartCard(nickname, virtualClient, placeOnFront);
    }

    @Override
    public boolean isCheck() {
        return false;
    }
}
