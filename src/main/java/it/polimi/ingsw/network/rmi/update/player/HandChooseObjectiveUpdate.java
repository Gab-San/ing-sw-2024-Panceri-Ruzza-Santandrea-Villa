package it.polimi.ingsw.network.rmi.update.player;

import it.polimi.ingsw.view.ModelUpdater;

/**
 * This class represents a rmi update.
 * <p>
 *     Notifies on the choice of the objective card.
 * </p>
 */
public class HandChooseObjectiveUpdate extends PlayerTypeUpdate{
    private final String chosenObjectiveId;

    /**
     * Constructs the update.
     * @param modelUpdater instance of the model updater referenced to execute updates
     * @param nickname unique id of the player
     * @param chosenObjectiveId identifier of the chosen card
     */
    public HandChooseObjectiveUpdate(ModelUpdater modelUpdater, String nickname, String chosenObjectiveId) {
        super(modelUpdater, nickname);
        this.chosenObjectiveId = chosenObjectiveId;
    }

    /**
     * Updates the view removing the non-chosen card.
     */
    @Override
    public void update() {
        modelUpdater.playerHandChooseObject(nickname, chosenObjectiveId);
    }
}
