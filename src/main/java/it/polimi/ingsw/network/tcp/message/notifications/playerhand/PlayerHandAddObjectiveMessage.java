package it.polimi.ingsw.network.tcp.message.notifications.playerhand;

import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.tcp.message.notifications.player.PlayerMessage;

import java.io.Serial;
import java.rmi.RemoteException;

/**
 * This class represents a player message. Carries information about an objective card added to the player's hand.
 */
public class PlayerHandAddObjectiveMessage extends PlayerMessage {
    @Serial
    private static final long serialVersionUID = -468435213351L;
    private final String objectiveCard;

    /**
     * Constructs the hand added objective message.
     * @param nickname associated player's unique id
     * @param objectiveCard objective card's id added in the player's hand
     */
    public PlayerHandAddObjectiveMessage(String nickname, String objectiveCard) {
        super(nickname);
        this.objectiveCard = objectiveCard;
    }

    @Override
    public void execute(VirtualClient virtualClient) throws RemoteException {
        virtualClient.playerHandAddObjective(nickname, objectiveCard);
    }
}
