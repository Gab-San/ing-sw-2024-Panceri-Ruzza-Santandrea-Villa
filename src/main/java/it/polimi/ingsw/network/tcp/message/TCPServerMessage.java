package it.polimi.ingsw.network.tcp.message;

import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

/**
 * This interface represents messages sent by the client to the server.
 */
public interface TCPServerMessage extends TCPMessage {
    /**
     * Executes the message inner action.
     * @param virtualClient client on which the message arrived
     * @throws RemoteException if an error occurs while executing remote calls.
     */
    void execute(VirtualClient virtualClient) throws RemoteException;
}
