package it.polimi.ingsw.server.tcp.message;

import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.VirtualServer;

import java.io.Serial;
import java.rmi.RemoteException;

public class RestartGameMessage implements TCPClientMessage {
    @Serial
    private static final long serialVersionUID = 73L;
    private final String nickname;

    public RestartGameMessage(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public void execute(VirtualServer virtualServer, VirtualClient virtualClient) throws RemoteException {
        virtualServer.startGame(nickname, virtualClient);
    }

    @Override
    public String toString() {
        return "RESTART GAME";
    }

    @Override
    public boolean isError() {
        return false;
    }
}
