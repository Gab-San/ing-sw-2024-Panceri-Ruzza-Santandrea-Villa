package it.polimi.ingsw.network.tcp.message.notifications.board;

import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.tcp.message.notifications.player.PlayerMessage;

import java.io.Serial;
import java.rmi.RemoteException;

/**
 * This class implements tcp server message interface.
 * Sent when a player deadlock status changed.
 */
public class PlayerDeadLockMessage extends PlayerMessage {
    @Serial
    private static final long serialVersionUID = 194827192487L;
    private final boolean isDeadLocked;

    /**
     * Constructs player deadlock message.
     * @param nickname unique identifier of the player
     * @param isDeadLocked true if the player is deadlocked, false otherwise
     */
    public PlayerDeadLockMessage(String nickname, boolean isDeadLocked) {
        super(nickname);
        this.isDeadLocked = isDeadLocked;
    }

    @Override
    public boolean isCheck() {
        return false;
    }

    @Override
    public void execute(VirtualClient virtualClient) throws RemoteException {
        virtualClient.playerDeadLockUpdate(nickname, isDeadLocked);
    }
}
