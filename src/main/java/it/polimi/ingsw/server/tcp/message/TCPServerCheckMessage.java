package it.polimi.ingsw.server.tcp.message;

import it.polimi.ingsw.server.tcp.client.ServerProxy;

public interface TCPServerCheckMessage extends TCPMessage {
    void handle(ServerProxy client) throws IllegalStateException, IllegalArgumentException;
}
