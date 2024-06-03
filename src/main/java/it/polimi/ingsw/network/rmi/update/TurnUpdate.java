package it.polimi.ingsw.network.rmi.update;

import it.polimi.ingsw.view.ModelUpdater;

/**
 * This class represents a rmi update.
 * <p>
 *  It notifies about a change of turn.
 * </p>
 */
public class TurnUpdate extends RMIUpdate {
    private final int currentTurn;

    /**
     * Constructs the update.
     * @param modelUpdater instance of the model updater referenced to execute updates
     * @param currentTurn match's updated current turn
     */
    public TurnUpdate(ModelUpdater modelUpdater, int currentTurn) {
        super(modelUpdater);
        this.currentTurn = currentTurn;
    }

    /**
     * Updates the view aligning the local turn to the server current turn.
     */
    @Override
    public void update() {
        modelUpdater.updateTurn(currentTurn);
    }
}
