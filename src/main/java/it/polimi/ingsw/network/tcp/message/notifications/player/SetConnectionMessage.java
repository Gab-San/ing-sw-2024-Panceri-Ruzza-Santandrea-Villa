package it.polimi.ingsw.network.tcp.message.notifications.player;

import it.polimi.ingsw.network.VirtualClient;

import java.io.Serial;
import java.rmi.RemoteException;

public class SetConnectionMessage extends PlayerMessage {
    @Serial
    private static final long serialVersionUID = 5108721908347L;
    private final boolean isConnected;
    public SetConnectionMessage(String nickname, boolean isConnected) {
        super(nickname);
        this.isConnected = isConnected;
    }



    @Override
    public void execute(VirtualClient virtualClient) throws RemoteException {
        virtualClient.updatePlayer(nickname, isConnected);
    }
}
