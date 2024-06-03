package it.polimi.ingsw.network.tcp.message;

import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

/**
 * This interface represents messages sent by the client to the server.
 */
public interface TCPServerMessage extends TCPMessage {
    void execute(VirtualClient virtualClient) throws RemoteException;
}
