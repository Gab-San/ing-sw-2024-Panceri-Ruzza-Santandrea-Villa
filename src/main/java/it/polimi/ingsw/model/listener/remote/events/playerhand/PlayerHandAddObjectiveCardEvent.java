package it.polimi.ingsw.model.listener.remote.events.playerhand;

import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.listener.remote.events.player.PlayerEvent;
import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;
/**
 * This class represents a player event. It is triggered when an objective card is added to a player's hand.
 */
public class PlayerHandAddObjectiveCardEvent extends PlayerEvent {
    private final String objectiveCard;

    /**
     * Constructs the add objective card event.
     * @param nickname player's id
     * @param objectiveCard added card
     */
    public PlayerHandAddObjectiveCardEvent(String nickname, ObjectiveCard objectiveCard) {
        super(nickname);
        this.objectiveCard = objectiveCard.getCardID();
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.playerHandAddObjective(nickname, objectiveCard);
    }
}
