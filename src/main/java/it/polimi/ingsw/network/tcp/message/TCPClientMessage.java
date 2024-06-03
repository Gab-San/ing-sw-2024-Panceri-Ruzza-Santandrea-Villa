package it.polimi.ingsw.network.tcp.message;

import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.VirtualServer;

import java.rmi.RemoteException;

/**
 * This interface describes messages sent by the client to the server.
 */
public interface TCPClientMessage extends TCPMessage {
    /**
     * Executes the message inner action.
     * @param virtualServer server at which the message was sent
     * @param virtualClient server-side proxy bound to client
     * @throws IllegalStateException details may vary depending on implementation
     * @throws IllegalArgumentException details may vary depending on implementation
     * @throws RemoteException if an error occurs while executing remote calls.
     */
    void execute(VirtualServer virtualServer, VirtualClient virtualClient)
            throws IllegalStateException, IllegalArgumentException, RemoteException;
}
