package it.polimi.ingsw.network.tcp.message.notifications.playerhand;

import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.tcp.message.notifications.player.PlayerMessage;

import java.io.Serial;
import java.rmi.RemoteException;

public class PlayerHandChooseObjectiveMessage extends PlayerMessage {
    @Serial
    private static final long serialVersionUID = -768465465434L;
    private final String chosenObjectiveId;
    public PlayerHandChooseObjectiveMessage(String nickname, String chosenObjectiveId) {
        super(nickname);
        this.chosenObjectiveId = chosenObjectiveId;
    }

    @Override
    public void execute(VirtualClient virtualClient) throws RemoteException {
        virtualClient.playerHandChooseObject(nickname, chosenObjectiveId);
    }
}
