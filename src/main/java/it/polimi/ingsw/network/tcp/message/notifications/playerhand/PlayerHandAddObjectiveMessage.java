package it.polimi.ingsw.network.tcp.message.notifications.playerhand;

import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.tcp.message.notifications.player.PlayerMessage;

import java.rmi.RemoteException;

public class PlayerHandAddObjectiveMessage extends PlayerMessage {
    private final String objectiveCard;
    public PlayerHandAddObjectiveMessage(String nickname, String objectiveCard) {
        super(nickname);
        this.objectiveCard = objectiveCard;
    }

    @Override
    public void execute(VirtualClient virtualClient) throws RemoteException {
        virtualClient.playerHandAddObjective(nickname, objectiveCard);
    }
}
