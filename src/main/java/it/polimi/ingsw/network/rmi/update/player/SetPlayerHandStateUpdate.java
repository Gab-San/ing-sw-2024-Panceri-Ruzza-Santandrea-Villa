package it.polimi.ingsw.network.rmi.update.player;

import it.polimi.ingsw.view.ModelUpdater;

import java.util.List;

/**
 * This class represents a rmi update.
 * <p>
 *     It notifies of the current state of the player's hand.
 * </p>
 */
public class SetPlayerHandStateUpdate extends PlayerTypeUpdate{
    private final List<String> playCards;
    private final List<String> objectiveCards;
    private final String startingCard;

    /**
     * Constructs the update
     * @param modelUpdater instance of the model updater referenced to execute updates
     * @param nickname unique id of the hand's player
     * @param playCards list of playable cards in hand
     * @param objectiveCards objective cards in hand
     * @param startingCard starting card in hand
     */
    public SetPlayerHandStateUpdate(ModelUpdater modelUpdater, String nickname, List<String> playCards, List<String> objectiveCards, String startingCard) {
        super(modelUpdater, nickname);
        this.playCards = playCards;
        this.objectiveCards = objectiveCards;
        this.startingCard = startingCard;
    }

    /**
     * Updates the view initialising the specified player's hand
     */
    @Override
    public void update() {
        modelUpdater.setPlayerHandState(nickname, playCards, objectiveCards, startingCard);
    }
}
