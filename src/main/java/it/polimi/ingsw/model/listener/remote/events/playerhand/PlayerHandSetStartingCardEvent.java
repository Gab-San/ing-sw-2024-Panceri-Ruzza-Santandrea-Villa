package it.polimi.ingsw.model.listener.remote.events.playerhand;

import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.model.listener.remote.events.player.PlayerEvent;
import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

public class PlayerHandSetStartingCardEvent extends PlayerEvent {
    private final String startingCardId;
    public PlayerHandSetStartingCardEvent(String nickname, StartingCard startingCard) {
        super(nickname);
        this.startingCardId = (startingCard == null) ? null : startingCard.getCardID();
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.playerHandSetStartingCard(nickname, startingCardId);
    }

    @Override
    public String toString() {
        return  super.toString() +
                "\nPlayerHandSetStartingCardEvent{" +
                "startingCardId='" + startingCardId + '\'' +
                '}';
    }
}
