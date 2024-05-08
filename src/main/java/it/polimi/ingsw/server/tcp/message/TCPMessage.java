package it.polimi.ingsw.server.tcp.message;

import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.tcp.ClientHandler;

import java.io.Serializable;
import java.util.function.Consumer;

public interface TCPMessage extends Serializable {

    Consumer<ClientHandler> getCommand(VirtualClient virtualClient);
    boolean isExit();
}
