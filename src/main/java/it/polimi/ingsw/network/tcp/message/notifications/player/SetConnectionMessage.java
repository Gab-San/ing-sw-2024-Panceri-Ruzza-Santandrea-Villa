package it.polimi.ingsw.network.tcp.message.notifications.player;

import it.polimi.ingsw.network.VirtualClient;

import java.io.Serial;
import java.rmi.RemoteException;

/**
 * This class implements a player message. Carries information about the connection status of a player.
 */
public class SetConnectionMessage extends PlayerMessage {
    @Serial
    private static final long serialVersionUID = 5108721908347L;
    private final boolean isConnected;

    /**
     * Constructs player connection message.
     * @param nickname player's unique id
     * @param isConnected true if the player is connected, false otherwise
     */
    public SetConnectionMessage(String nickname, boolean isConnected) {
        super(nickname);
        this.isConnected = isConnected;
    }

    @Override
    public void execute(VirtualClient virtualClient) throws RemoteException {
        virtualClient.updatePlayer(nickname, isConnected);
    }
}
