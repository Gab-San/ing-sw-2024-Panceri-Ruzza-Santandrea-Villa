package it.polimi.ingsw.model.listener.remote.events.playerhand;

import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.listener.remote.events.player.PlayerEvent;
import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

public class PlayerHandAddObjectiveCardEvent extends PlayerEvent {
    private final String objectiveCard;
    public PlayerHandAddObjectiveCardEvent(String nickname, ObjectiveCard objectiveCard) {
        super(nickname);
        this.objectiveCard = objectiveCard.getCardID();
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.playerHandAddObjective(nickname, objectiveCard);
    }
}
