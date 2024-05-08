package it.polimi.ingsw.server.tcp.message;

import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.tcp.ClientHandler;

import java.io.Serial;
import java.rmi.RemoteException;
import java.util.function.Consumer;

public class TestMessage implements TCPMessage{
    @Serial
    private static final long serialVersionUID = 0000L;
    private final String nickname;
    private final String message;

    public TestMessage(String nickname, String message){
        this.nickname = nickname;
        this.message = message;
    }

    @Override
    public Consumer<ClientHandler> getCommand(VirtualClient virtualClient) {
        return (server) -> {
            try {
                server.testCmd(nickname, virtualClient,  message);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        };
    }

    @Override
    public boolean isExit() {
        return false;
    }
}
