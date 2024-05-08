package it.polimi.ingsw.server.tcp.message;

import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.tcp.ClientHandler;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.function.Consumer;

public class ConnectMessage implements TCPMessage{
    private final String nickname;

    public ConnectMessage(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public Consumer<ClientHandler> getCommand(VirtualClient virtualClient) {
        return (clientHandler) -> {
                try {
                    clientHandler.connect(nickname, virtualClient);
                    virtualClient.update("OK");
                } catch (IllegalStateException exception) {
                    try {
                        virtualClient.update("ERROR " + exception.getMessage());
                    } catch (RemoteException e) {
                        clientHandler.tearDown();
                    }
                } catch (RemoteException e) {
                    clientHandler.tearDown();
                }
        };
    }

    @Override
    public boolean isExit() {
        return false;
    }
}
