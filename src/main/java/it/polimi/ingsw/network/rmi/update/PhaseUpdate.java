package it.polimi.ingsw.network.rmi.update;

import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.view.ModelUpdater;

public class PhaseUpdate extends RMIUpdate {
    private final GamePhase gamePhase;
    public PhaseUpdate(ModelUpdater modelUpdater, GamePhase gamePhase) {
        super(modelUpdater);
        this.gamePhase = gamePhase;
    }

    @Override
    public void update() {
        modelUpdater.updatePhase(gamePhase);
    }
}
