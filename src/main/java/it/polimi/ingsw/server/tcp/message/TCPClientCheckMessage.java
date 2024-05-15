package it.polimi.ingsw.server.tcp.message;

import it.polimi.ingsw.server.tcp.server.ClientHandler;
import it.polimi.ingsw.server.tcp.message.TCPMessage;

public interface TCPClientCheckMessage extends TCPMessage {
    void handle(ClientHandler server);
}
