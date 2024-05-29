package it.polimi.ingsw.network.rmi.update;

import it.polimi.ingsw.view.ModelUpdater;

public class NotifyIndirectDisconnect extends RMIUpdate{
    public NotifyIndirectDisconnect(ModelUpdater modelUpdater) {
        super(modelUpdater);
    }

    @Override
    public void update() {
        modelUpdater.notifyIndirectDisconnect();
    }
}
