package it.polimi.ingsw.server.tcp.message;

import com.diogonunes.jcolor.Attribute;
import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.VirtualServer;

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
        System.out.println(colorize("Pinging server...", Attribute.MAGENTA_TEXT()));
        virtualServer.ping();
    }

    @Override
    public void execute(VirtualClient virtualClient) throws RemoteException {
        System.out.println(colorize("Pinging client...", Attribute.BLUE_TEXT()));
        virtualClient.ping();
    }
}
