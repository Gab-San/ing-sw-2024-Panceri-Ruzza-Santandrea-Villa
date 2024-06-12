package it.polimi.ingsw.network.tcp.message.notifications.player;

import it.polimi.ingsw.network.VirtualClient;

import java.io.Serial;
import java.rmi.RemoteException;

/**
 * This class inherits from player message. Carries information about player's removal.
 */
public class PlayerRemovalMessage extends PlayerMessage {
    @Serial
    private static final long serialVersionUID = 5197619876213L;

    /**
     * Constructs player removal message.
     * @param nickname removed player's nickname
     */
    public PlayerRemovalMessage(String nickname) {
        super(nickname);
    }


    @Override
    public void execute(VirtualClient virtualClient) throws RemoteException {
        virtualClient.removePlayer(nickname);
    }
}
