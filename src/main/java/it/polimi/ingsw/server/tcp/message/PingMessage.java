package it.polimi.ingsw.server.tcp.message;

import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.VirtualServer;

import java.io.Serial;
import java.rmi.RemoteException;

public class PingMessage implements TCPClientMessage, TCPServerMessage {
    @Serial
    private static final long serialVersionUID = 0000L;

    @Override
    public boolean isCheck() {
        return false;
    }

    @Override
    public void execute(VirtualServer virtualServer, VirtualClient virtualClient) throws RemoteException {
        virtualServer.ping();
    }

    @Override
    public void execute(VirtualClient virtualClient) throws RemoteException {
        virtualClient.ping();
    }
}
