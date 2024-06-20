package it.polimi.ingsw.model.listener.remote.events.playerhand;

import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.listener.remote.events.player.PlayerEvent;
import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;
/**
 * This class represents a player event. It triggers as the player chooses their objective card.
 */
public class PlayerHandChooseObjectiveCardEvent extends PlayerEvent {
    private final String chosenObjectiveId;

    /**
     * Constructs choose objective card event.
     * @param nickname hand owner's id
     * @param chosenCard chosen secret card
     */
    public PlayerHandChooseObjectiveCardEvent(String nickname, ObjectiveCard chosenCard) {
        super(nickname);
        chosenObjectiveId = chosenCard.getCardID();
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.playerHandChooseObject(nickname, chosenObjectiveId);
    }
}
