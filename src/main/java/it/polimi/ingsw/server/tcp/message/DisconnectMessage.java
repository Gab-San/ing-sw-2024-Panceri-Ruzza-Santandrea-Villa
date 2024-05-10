package it.polimi.ingsw.server.tcp.message;

import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.VirtualServer;

import java.io.Serial;
import java.rmi.RemoteException;

public class DisconnectMessage implements TCPClientMessage{
    @Serial
    private static final long serialVersionUID = 1000L;
    private final String nickname;

    public DisconnectMessage(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public void execute(VirtualServer virtualServer, VirtualClient virtualClient) throws RemoteException {
        virtualServer.disconnect(nickname, virtualClient);
    }

    @Override
    public String toString() {
        return "DISCONNECT";
    }

    @Override
    public boolean isError() {
        return false;
    }
}
