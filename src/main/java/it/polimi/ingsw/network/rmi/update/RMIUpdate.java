package it.polimi.ingsw.network.rmi.update;

import it.polimi.ingsw.view.ModelUpdater;

abstract public class RMIUpdate {
    protected final ModelUpdater modelUpdater;

    protected RMIUpdate(ModelUpdater modelUpdater) {
        this.modelUpdater = modelUpdater;
    }

    public abstract void update();
}
