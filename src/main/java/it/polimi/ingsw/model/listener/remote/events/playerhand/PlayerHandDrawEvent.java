package it.polimi.ingsw.model.listener.remote.events.playerhand;

import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.listener.remote.events.player.PlayerEvent;
import it.polimi.ingsw.server.VirtualClient;

import java.rmi.RemoteException;

public class PlayerHandDrawEvent extends PlayerEvent {
    private final String drawnCardId;
    public PlayerHandDrawEvent(String nickname, PlayCard drawnCard) {
        super(nickname);
        this.drawnCardId = drawnCard.getCardID();
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.playerHandAddedCardUpdate(nickname, drawnCardId);
    }
}
