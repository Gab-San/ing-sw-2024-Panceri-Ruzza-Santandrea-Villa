package it.polimi.ingsw.network.tcp.message.notifications.board;

import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.tcp.message.notifications.player.PlayerMessage;

import java.io.Serial;
import java.rmi.RemoteException;

public class PlayerDeadLockMessage extends PlayerMessage {
    @Serial
    private static final long serialVersionUID = 194827192487L;
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
