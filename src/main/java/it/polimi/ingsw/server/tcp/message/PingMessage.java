package it.polimi.ingsw.server.tcp.message;

import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.tcp.ClientHandler;

import java.rmi.RemoteException;
import java.util.function.Consumer;

public class PingMessage implements TCPMessage{
    private final boolean fromClient;
    public PingMessage(){
        this.fromClient = true;
    }

    public PingMessage(boolean fromClient){
        this.fromClient = fromClient;
    }
    @Override
    public Consumer<ClientHandler> getCommand(VirtualClient virtualClient) {
        if(!fromClient){
            return (clientHandler) -> {
                try {
                    virtualClient.ping();
                } catch (RemoteException e) {
                    virtualClient.close();
                }
            };
        }
        return (clientHandler) ->{
            try {
                clientHandler.ping();
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
