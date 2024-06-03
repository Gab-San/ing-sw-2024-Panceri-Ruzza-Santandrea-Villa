package it.polimi.ingsw.network.rmi.update.player;

import it.polimi.ingsw.view.ModelUpdater;

/**
 * This class represents a rmi update.
 * <p>
 *     Notifies the addition of an objetive card to the player's hand
 * </p>
 */
public class HandAddObjectiveUpdate extends PlayerTypeUpdate{
    private final String objectiveCard;

    /**
     * Constructs the update.
     * @param modelUpdater instance of the model updater referenced to execute updates
     * @param nickname unique id of the player
     * @param objectiveCard identifier of the added objective card
     */
    public HandAddObjectiveUpdate(ModelUpdater modelUpdater, String nickname, String objectiveCard) {
        super(modelUpdater, nickname);
        this.objectiveCard = objectiveCard;
    }

    /**
     * Updates the view adding a card to the player hand.
     */
    @Override
    public void update() {
        modelUpdater.playerHandAddObjective(nickname, objectiveCard);
    }
}
