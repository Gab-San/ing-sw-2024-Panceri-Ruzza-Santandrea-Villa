package it.polimi.ingsw.server.tcp.message;

import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.VirtualServer;

import java.io.Serial;
import java.rmi.RemoteException;

public class ConnectMessage implements TCPClientMessage{
    @Serial
    private static final long serialVersionUID = 1L;
    private final String nickname;
    public ConnectMessage(String nickname){
        this.nickname = nickname;
    }

    @Override
    public void execute(VirtualServer virtualServer, VirtualClient virtualClient) throws RemoteException {
        virtualServer.connect(nickname, virtualClient);
    }

    @Override
    public String toString() {
        return "CONNECT";
    }

    @Override
    public boolean isError() {
        return false;
    }
}
