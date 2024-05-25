package it.polimi.ingsw.network.tcp.message.notifications.board;

import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.tcp.message.notifications.PlayerNotificationMessage;

import java.rmi.RemoteException;

public class PlayerDeadLockMessage extends PlayerNotificationMessage {
    private final boolean isDeadLocked;
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
