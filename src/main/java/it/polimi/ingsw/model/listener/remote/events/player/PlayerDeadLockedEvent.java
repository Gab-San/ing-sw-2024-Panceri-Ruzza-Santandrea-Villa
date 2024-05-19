package it.polimi.ingsw.model.listener.remote.events.player;

import it.polimi.ingsw.server.VirtualClient;

import java.rmi.RemoteException;

public class PlayerDeadLockedEvent extends PlayerEvent{
    private final boolean isDeadLocked;
    public PlayerDeadLockedEvent(String nickname, boolean isDeadLocked) {
        super(nickname);
        this.isDeadLocked = isDeadLocked;
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.playerDeadLockUpdate(nickname, isDeadLocked);
    }
}
