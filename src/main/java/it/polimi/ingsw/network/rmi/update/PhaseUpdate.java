package it.polimi.ingsw.network.rmi.update;

import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.view.ModelUpdater;

/**
 * This class represents a rmi update.
 * <p>
 *     Updates local phase to match server phase.
 * </p>
 */
public class PhaseUpdate extends RMIUpdate {
    private final GamePhase gamePhase;

    /**
     * Constructs the update.
     * @param modelUpdater instance of the model updater referenced to execute updates
     * @param gamePhase current game phase reached in the match
     */
    public PhaseUpdate(ModelUpdater modelUpdater, GamePhase gamePhase) {
        super(modelUpdater);
        this.gamePhase = gamePhase;
    }

    /**
     * Updates the view notifying about the new game phased reached.
     */
    @Override
    public void update() {
        modelUpdater.updatePhase(gamePhase);
    }
}
