package it.polimi.ingsw.model.listener.remote.events.playerhand;

import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.listener.remote.events.player.PlayerEvent;
import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

public class PlayerHandRemoveCardEvent extends PlayerEvent {
    private final String playCardId;
    public PlayerHandRemoveCardEvent(String nickname, PlayCard playCard) {
        super(nickname);
        playCardId = playCard.getCardID();
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.playerHandRemoveCard(nickname, playCardId);
    }
}
