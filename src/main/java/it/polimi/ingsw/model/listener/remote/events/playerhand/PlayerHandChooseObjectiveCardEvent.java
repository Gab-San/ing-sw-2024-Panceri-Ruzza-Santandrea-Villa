package it.polimi.ingsw.model.listener.remote.events.playerhand;

import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.listener.remote.events.player.PlayerEvent;
import it.polimi.ingsw.server.VirtualClient;

import java.rmi.RemoteException;

public class PlayerHandChooseObjectiveCardEvent extends PlayerEvent {
    private final String chosenObjectiveId;
    public PlayerHandChooseObjectiveCardEvent(String nickname, ObjectiveCard chosenCard) {
        super(nickname);
        chosenObjectiveId = chosenCard.getCardID();
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.playerHandChooseObject(nickname, chosenObjectiveId);
    }
}
