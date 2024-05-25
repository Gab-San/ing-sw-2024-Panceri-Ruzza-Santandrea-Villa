package it.polimi.ingsw.network.tcp.message.notifications.playerhand;

import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.tcp.message.notifications.player.PlayerMessage;

import java.rmi.RemoteException;

public class PlayerHandSetStartingCardMessage extends PlayerMessage {
    private final String startingCardId;
    public PlayerHandSetStartingCardMessage(String nickname, String startingCardId) {
        super(nickname);
        this.startingCardId = startingCardId;
    }

    @Override
    public void execute(VirtualClient virtualClient) throws RemoteException {
        virtualClient.playerHandSetStartingCard(nickname, startingCardId);
    }
}
