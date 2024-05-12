package it.polimi.ingsw.server.tcp.message;

import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.VirtualServer;

import java.rmi.RemoteException;

public class PlaceStartingCardMessage implements TCPClientMessage{

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
    public boolean isError() {
        return false;
    }
}
