package it.polimi.ingsw.network.rmi.update;

import it.polimi.ingsw.view.ModelUpdater;

/**
 * This class represents a rmi update.
 *
 * <p>
 *     It notifies about all asynchronous errors except for indirect disconnection.
 * </p>
 */
public class ReportErrorUpdate extends RMIUpdate {
    private final String errorMessage;

    /**
     * Constructs the update.
     * @param modelUpdater instance of the model updater referenced to execute updates
     * @param errorMessage error message to display
     */
    public ReportErrorUpdate(ModelUpdater modelUpdater, String errorMessage) {
        super(modelUpdater);
        this.errorMessage = errorMessage;
    }

    /**
     * Updates the view notifiying about an asynchronous error.
     */
    @Override
    public void update() {
        modelUpdater.reportError(errorMessage);
    }
}
