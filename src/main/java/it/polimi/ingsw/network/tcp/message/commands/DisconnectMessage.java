package it.polimi.ingsw.network.tcp.message.commands;

import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.VirtualServer;
import it.polimi.ingsw.network.tcp.message.TCPClientMessage;

import java.io.Serial;
import java.rmi.RemoteException;

/**
 * This class implements tcp client message. Sent when disconnect action is requested.
 */
public class DisconnectMessage implements TCPClientMessage {
    @Serial
    private static final long serialVersionUID = 1000L;
    private final String nickname;

    /**
     * Constructs the disconnection message.
     * @param nickname unique id of the user
     */
    public DisconnectMessage(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public void execute(VirtualServer virtualServer, VirtualClient virtualClient)
            throws IllegalStateException, IllegalArgumentException, RemoteException {
        virtualServer.disconnect(nickname, virtualClient);
    }

    @Override
    public boolean isCheck() {
        return false;
    }
}
