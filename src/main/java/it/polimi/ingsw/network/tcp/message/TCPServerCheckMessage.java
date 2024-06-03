package it.polimi.ingsw.network.tcp.message;

import it.polimi.ingsw.network.tcp.client.ServerProxy;

/**
 * This interface portrays the checks sent from the server to the client.
 */
public interface TCPServerCheckMessage extends TCPMessage {
    void handle(ServerProxy client) throws IllegalStateException, IllegalArgumentException;
}
