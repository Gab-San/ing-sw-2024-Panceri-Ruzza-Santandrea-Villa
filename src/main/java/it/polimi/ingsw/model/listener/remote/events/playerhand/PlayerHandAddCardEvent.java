package it.polimi.ingsw.model.listener.remote.events.playerhand;

import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.listener.remote.events.player.PlayerEvent;
import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

/**
 * This class represents a player event. It is triggered when a card is added to the player hand.
 */
public class PlayerHandAddCardEvent extends PlayerEvent {
    private final String drawnCardId;

    /**
     * Constructs the added card event.
     * @param nickname hand owner's id
     * @param drawnCard added card
     */
    public PlayerHandAddCardEvent(String nickname, PlayCard drawnCard) {
        super(nickname);
        this.drawnCardId = drawnCard.getCardID();
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.playerHandAddedCardUpdate(nickname, drawnCardId);
    }
}
