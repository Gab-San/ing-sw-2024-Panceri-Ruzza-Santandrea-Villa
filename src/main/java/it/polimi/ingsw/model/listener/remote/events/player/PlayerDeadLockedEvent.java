package it.polimi.ingsw.model.listener.remote.events.player;

import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

/**
 * This class represents a player event. It is triggered by a deadlock being updated.
 */
public class PlayerDeadLockedEvent extends PlayerEvent{
    private final boolean isDeadLocked;

    /**
     * Constructs a player deadlock event.
     * @param nickname player's id
     * @param isDeadLocked true if the player is deadlocked, false otherwise
     */
    public PlayerDeadLockedEvent(String nickname, boolean isDeadLocked) {
        super(nickname);
        this.isDeadLocked = isDeadLocked;
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.playerDeadLockUpdate(nickname, isDeadLocked);
    }
}
