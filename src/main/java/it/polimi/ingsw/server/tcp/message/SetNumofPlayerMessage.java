package it.polimi.ingsw.server.tcp.message;

import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.VirtualServer;

import java.io.Serial;
import java.rmi.RemoteException;

public class SetNumofPlayerMessage implements TCPClientMessage{
    @Serial
    private static final long serialVersionUID = 8L;
    private final String nickname;
    private final int numOfPlayers;

    public SetNumofPlayerMessage(String nickname, int numOfPlayers) {
        this.nickname = nickname;
        this.numOfPlayers = numOfPlayers;
    }

    @Override
    public void execute(VirtualServer virtualServer, VirtualClient virtualClient) throws RemoteException {
        virtualServer.setNumOfPlayers(nickname,virtualClient,numOfPlayers);
    }
}
