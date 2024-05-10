package it.polimi.ingsw.server.tcp.message;

import it.polimi.ingsw.server.tcp.TCPClient;

public interface TCPServerErrorMessage extends TCPMessage{
    void handle(TCPClient client);
}
