package it.polimi.ingsw.network.tcp.message.commands;

import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.VirtualServer;
import it.polimi.ingsw.network.tcp.message.TCPClientMessage;
import it.polimi.ingsw.network.tcp.message.TCPServerMessage;

import java.io.Serial;
import java.rmi.RemoteException;

public class SendMessage implements TCPClientMessage, TCPServerMessage {
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

    @Override
    public void execute(VirtualClient virtualClient) throws RemoteException {
        virtualClient.update(message);
    }

    @Override
    public boolean isCheck() {
        return false;
    }
}
