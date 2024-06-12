package it.polimi.ingsw.network.tcp.message.notifications.playerhand;

import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.tcp.message.notifications.player.PlayerMessage;

import java.io.Serial;
import java.rmi.RemoteException;
import java.util.List;

/**
 * This class represents a player message. It notifies about the initialization state of a player's hand.
 */
public class PlayerHandStateMessage extends PlayerMessage {
    @Serial
    private static final long serialVersionUID = 1561927360L;
    private final List<String> playCards;
    private final List<String> objectiveCards;
    private final String startingCard;

    /**
     * Constructs the player hand state message.
     * @param nickname associated player's unique id
     * @param playCards list of playable cards in player's hand
     * @param objectiveCards list of objective cards in player's hand (might be more than one)
     * @param startingCard starting card in player's hand
     */
    public PlayerHandStateMessage(String nickname, List<String> playCards,
                                  List<String> objectiveCards, String startingCard) {
        super(nickname);
        this.playCards = playCards;
        this.objectiveCards = objectiveCards;
        this.startingCard = startingCard;
    }

    @Override
    public void execute(VirtualClient virtualClient) throws RemoteException {
        virtualClient.setPlayerHandState(nickname, playCards, objectiveCards, startingCard);
    }
}
