package it.polimi.ingsw.network.rmi.update;

import it.polimi.ingsw.model.listener.remote.UpdateTask;
import it.polimi.ingsw.view.ModelUpdater;

public class ReportErrorUpdate extends RMIUpdate {
    private final String errorMessage;
    public ReportErrorUpdate(ModelUpdater modelUpdater, String errorMessage) {
        super(modelUpdater);
        this.errorMessage = errorMessage;
    }

    @Override
    public void update() {
        modelUpdater.reportError(errorMessage);
    }
}
