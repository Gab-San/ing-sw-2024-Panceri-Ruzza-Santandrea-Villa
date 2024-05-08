package it.polimi.ingsw.server.tcp.message;

import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.tcp.ClientHandler;

import java.util.function.Consumer;

public class ExitMessage implements TCPMessage{
    @Override
    public Consumer<ClientHandler> getCommand(VirtualClient virtualClient) {
        return null;
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
