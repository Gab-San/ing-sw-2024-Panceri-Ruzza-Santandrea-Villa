package it.polimi.ingsw.model.listener.remote.events.playerhand;

import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.listener.remote.events.player.PlayerEvent;
import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

public class PlayerHandAddCardEvent extends PlayerEvent {
    private final String drawnCardId;
    public PlayerHandAddCardEvent(String nickname, PlayCard drawnCard) {
        super(nickname);
        this.drawnCardId = drawnCard.getCardID();
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.playerHandAddedCardUpdate(nickname, drawnCardId);
    }
}
