package it.polimi.ingsw.network.tcp.message;

import it.polimi.ingsw.network.tcp.client.ServerProxy;

/**
 * This interface portrays the checks sent from the server to the client.
 */
public interface TCPServerCheckMessage extends TCPMessage {
    /**
     * Handles the error carried by the check.
     * @param client client on which to handle the error
     * @throws IllegalStateException if carried by the check message
     * @throws IllegalArgumentException if carried by the check message
     */
    void handle(ServerProxy client) throws IllegalStateException, IllegalArgumentException;
}
