package it.polimi.ingsw.model.listener.remote.events.playerhand;

import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.model.listener.remote.events.player.PlayerEvent;
import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

/**
 * This class represents a player event. It is triggered when the starting card is dealt to the player.
 */
public class PlayerHandSetStartingCardEvent extends PlayerEvent {
    private final String startingCardId;

    /**
     * Constructs set starting card event.
     * @param nickname hand owner's id
     * @param startingCard given starting card
     */
    public PlayerHandSetStartingCardEvent(String nickname, StartingCard startingCard) {
        super(nickname);
        this.startingCardId = (startingCard == null) ? null : startingCard.getCardID();
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.playerHandSetStartingCard(nickname, startingCardId);
    }
}
