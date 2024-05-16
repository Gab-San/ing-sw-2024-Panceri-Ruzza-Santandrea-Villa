package it.polimi.ingsw.server.tcp.message.commands;

import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.VirtualServer;
import it.polimi.ingsw.server.tcp.message.TCPClientMessage;

import java.io.Serial;
import java.rmi.RemoteException;

public class RestartGameMessage implements TCPClientMessage {
    @Serial
    private static final long serialVersionUID = 73L;
    private final String nickname;
    private final int numOfPlayers;

    public RestartGameMessage(String nickname, int numOfPlayers) {
        this.nickname = nickname;
        this.numOfPlayers = numOfPlayers;
    }

    @Override
    public void execute(VirtualServer virtualServer, VirtualClient virtualClient) throws RemoteException {
        virtualServer.restartGame(nickname, virtualClient, numOfPlayers);
    }

    @Override
    public boolean isCheck() {
        return false;
    }
}
