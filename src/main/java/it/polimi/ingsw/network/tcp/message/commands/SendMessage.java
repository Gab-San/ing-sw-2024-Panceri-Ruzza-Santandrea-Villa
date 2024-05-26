package it.polimi.ingsw.network.tcp.message.commands;

import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.VirtualServer;
import it.polimi.ingsw.network.tcp.message.TCPClientMessage;
import it.polimi.ingsw.network.tcp.message.TCPServerMessage;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.rmi.RemoteException;

public class SendMessage implements TCPClientMessage, TCPServerMessage {
    @Serial
    private static final long serialVersionUID = 72L;
    private final String messenger;
    private final String addressee;
    private final String message;
    public SendMessage(String messenger, String message){
        this.messenger = messenger;
        this.message = message;
        this.addressee = null;
    }

    public SendMessage(String messenger, @NotNull String addressee, String message) {
        this.messenger = messenger;
        this.addressee = addressee;
        this.message = message;
    }

    @Override
    public void execute(VirtualServer virtualServer, VirtualClient virtualClient) throws RemoteException {
        virtualServer.sendMsg(messenger, virtualClient, addressee, message);
    }

    @Override
    public void execute(VirtualClient virtualClient) throws RemoteException {
        virtualClient.displayMessage(messenger, message);
    }

    @Override
    public boolean isCheck() {
        return false;
    }
}
