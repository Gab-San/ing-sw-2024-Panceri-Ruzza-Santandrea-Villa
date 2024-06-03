package it.polimi.ingsw.network.rmi.update;

import it.polimi.ingsw.view.ModelUpdater;

/**
 * This class represents a rmi update.
 *<p>
 *     Notifies about indirect disconnection.
 *</p>
 */
public class NotifyIndirectDisconnect extends RMIUpdate{
    /**
     * Constructs the update.
     * @param modelUpdater instance of the model updater referenced to execute updates
     */
    public NotifyIndirectDisconnect(ModelUpdater modelUpdater) {
        super(modelUpdater);
    }

    /**
     * Updates the view notifying an indirect disconnection.
     */
    @Override
    public void update() {
        modelUpdater.notifyIndirectDisconnect();
    }
}
