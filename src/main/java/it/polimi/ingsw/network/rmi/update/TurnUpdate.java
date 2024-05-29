package it.polimi.ingsw.network.rmi.update;

import it.polimi.ingsw.view.ModelUpdater;

public class TurnUpdate extends RMIUpdate {
    private final int currentTurn;
    public TurnUpdate(ModelUpdater modelUpdater, int currentTurn) {
        super(modelUpdater);
        this.currentTurn = currentTurn;
    }

    @Override
    public void update() {
        modelUpdater.updateTurn(currentTurn);
    }
}
