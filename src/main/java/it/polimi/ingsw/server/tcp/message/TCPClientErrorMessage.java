package it.polimi.ingsw.server.tcp.message;

import it.polimi.ingsw.server.tcp.ClientHandler;

public interface TCPClientErrorMessage extends TCPMessage{
    void handle(ClientHandler server);
}
