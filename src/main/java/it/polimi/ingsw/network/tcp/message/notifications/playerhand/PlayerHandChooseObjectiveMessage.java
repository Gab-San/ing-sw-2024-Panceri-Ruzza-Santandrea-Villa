package it.polimi.ingsw.network.tcp.message.notifications.playerhand;

import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.tcp.message.notifications.player.PlayerMessage;

import java.io.Serial;
import java.rmi.RemoteException;

/**
 * This class implements the tcp server message interface and inherits from player message.
 * Notifies of the chosen objective card.
 */
public class PlayerHandChooseObjectiveMessage extends PlayerMessage {
    @Serial
    private static final long serialVersionUID = -768465465434L;
    private final String chosenObjectiveId;

    /**
     * Constructs chosen objective message.
     * @param nickname player's unique id
     * @param chosenObjectiveId chosen card's id
     */
    public PlayerHandChooseObjectiveMessage(String nickname, String chosenObjectiveId) {
        super(nickname);
        this.chosenObjectiveId = chosenObjectiveId;
    }

    @Override
    public void execute(VirtualClient virtualClient) throws RemoteException {
        virtualClient.playerHandChooseObject(nickname, chosenObjectiveId);
    }
}
