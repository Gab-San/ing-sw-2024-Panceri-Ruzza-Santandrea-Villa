package it.polimi.ingsw.network.rmi.update;

import it.polimi.ingsw.view.ModelUpdater;

/**
 * This class is used to lower abstraction from RMI to TCP.
 * <p>
 *     It defines an update issued by a RMI call.
 * </p>
 */
abstract public class RMIUpdate {
    /**
     * The instance of the model updater referenced to execute updates.
     */
    protected final ModelUpdater modelUpdater;

    /**
     * Constructs an update that will be executed by the specified model updater.
     * @param modelUpdater instance of the model updater referenced to execute updates
     */
    protected RMIUpdate(ModelUpdater modelUpdater) {
        this.modelUpdater = modelUpdater;
    }

    /**
     * Updates the view.
     */
    public abstract void update();
}
