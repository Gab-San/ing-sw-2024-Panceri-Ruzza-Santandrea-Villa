package it.polimi.ingsw.network.tcp.message;

import it.polimi.ingsw.network.tcp.client.ServerProxy;

public interface TCPServerCheckMessage extends TCPMessage {
    void handle(ServerProxy client) throws IllegalStateException, IllegalArgumentException;
}
