package it.polimi.ingsw.server.tcp.message;

import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.VirtualServer;

import java.io.Serial;
import java.rmi.RemoteException;

public class SendMessage implements TCPClientMessage {
    @Serial
    private static final long serialVersionUID = 72L;
    private final String nickname;
    private final String message;

    public SendMessage(String nickname, String message) {
        this.nickname = nickname;
        this.message = message;
    }

    @Override
    public void execute(VirtualServer virtualServer, VirtualClient virtualClient) throws RemoteException {
        virtualServer.sendMsg(nickname, virtualClient, message);
    }
}
