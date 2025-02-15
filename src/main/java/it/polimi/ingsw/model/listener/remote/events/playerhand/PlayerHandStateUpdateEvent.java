package it.polimi.ingsw.model.listener.remote.events.playerhand;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.model.listener.remote.events.player.PlayerEvent;
import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
/**
 * This class represents a player event. It notifies about the status of a player's hand.
 */
public class PlayerHandStateUpdateEvent extends PlayerEvent {
    private final List<String> playCards;
    private final List<String> objectiveCards;
    private final String startingCard;

    /**
     * Constructs the player hand state event.
     * @param nickname hand owner's id
     * @param playCards playable cards
     * @param objectiveCards secret card(s)
     * @param startingCard starting card
     */
    public PlayerHandStateUpdateEvent(String nickname, List<PlayCard> playCards,
                                      List<ObjectiveCard> objectiveCards, StartingCard startingCard) {
        super(nickname);
        this.playCards = new ArrayList<>(playCards.stream().map(Card::getCardID).toList());
        this.objectiveCards = new ArrayList<>(objectiveCards.stream().map(Card::getCardID).toList());
        this.startingCard = (startingCard == null) ? null : startingCard.getCardID();
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.setPlayerHandState(nickname, playCards, objectiveCards, startingCard);
    }
}
