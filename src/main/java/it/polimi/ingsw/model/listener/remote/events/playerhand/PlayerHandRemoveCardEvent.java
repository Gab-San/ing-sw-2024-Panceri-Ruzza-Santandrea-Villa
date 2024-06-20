package it.polimi.ingsw.model.listener.remote.events.playerhand;

import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.listener.remote.events.player.PlayerEvent;
import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;
/**
 * This class represents a player event. It is triggered when a card is removed from a player's hand.
 */
public class PlayerHandRemoveCardEvent extends PlayerEvent {
    private final String playCardId;

    /**
     * Constructs card removal event.
     * @param nickname hand owner's id
     * @param playCard removed card
     */
    public PlayerHandRemoveCardEvent(String nickname, PlayCard playCard) {
        super(nickname);
        playCardId = playCard.getCardID();
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.playerHandRemoveCard(nickname, playCardId);
    }
}
