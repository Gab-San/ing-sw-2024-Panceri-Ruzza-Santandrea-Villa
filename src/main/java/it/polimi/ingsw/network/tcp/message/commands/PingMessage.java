package it.polimi.ingsw.network.tcp.message.commands;

import com.diogonunes.jcolor.Attribute;
import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.VirtualServer;
import it.polimi.ingsw.network.tcp.message.TCPClientMessage;
import it.polimi.ingsw.network.tcp.message.TCPServerMessage;

import java.io.Serial;
import java.rmi.RemoteException;

import static com.diogonunes.jcolor.Ansi.colorize;

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
