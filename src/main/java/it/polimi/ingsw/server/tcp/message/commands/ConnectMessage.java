package it.polimi.ingsw.server.tcp.message.commands;

import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.VirtualServer;
import it.polimi.ingsw.server.tcp.message.TCPClientMessage;

import java.io.Serial;
import java.rmi.RemoteException;

public class ConnectMessage implements TCPClientMessage {
    @Serial
    private static final long serialVersionUID = 0001L;
    private final String nickname;
    public ConnectMessage(String nickname){
        this.nickname = nickname;
    }

    @Override
    public void execute(VirtualServer virtualServer, VirtualClient virtualClient) throws IllegalStateException, RemoteException {
        virtualServer.connect(nickname, virtualClient);
    }

    @Override
    public boolean isCheck() {
        return false;
    }
}
