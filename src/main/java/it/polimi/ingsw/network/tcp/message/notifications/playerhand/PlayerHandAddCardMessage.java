package it.polimi.ingsw.network.tcp.message.notifications.playerhand;

import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.tcp.message.notifications.player.PlayerMessage;

import java.io.Serial;
import java.rmi.RemoteException;

public class PlayerHandAddCardMessage extends PlayerMessage {
    @Serial
    private static final long serialVersionUID = 8751098237L;
    private final String drawnCardId;
    public PlayerHandAddCardMessage(String nickname, String drawnCardId) {
        super(nickname);
        this.drawnCardId = drawnCardId;
    }

    @Override
    public void execute(VirtualClient virtualClient) throws RemoteException {
        virtualClient.playerHandAddedCardUpdate(nickname, drawnCardId);
    }
}
