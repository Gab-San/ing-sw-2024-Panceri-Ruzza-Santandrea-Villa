package it.polimi.ingsw.network.tcp.message.commands;

import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.VirtualServer;
import it.polimi.ingsw.network.tcp.message.TCPClientMessage;

import java.io.Serial;
import java.rmi.RemoteException;

/**
 * This class implements tcp client message interface. Sent when a connection action is requested.
 * <p>
 *     It carries the necessary information to subscribe the user to the server.
 * </p>
 */
public class ConnectMessage implements TCPClientMessage {
    @Serial
    private static final long serialVersionUID = 0001L;
    private final String nickname;

    /**
     * Constructs the connect message.
     * @param nickname unique id of the user
     */
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
