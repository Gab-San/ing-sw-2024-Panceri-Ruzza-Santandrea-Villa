package it.polimi.ingsw.network.rmi.update.player;

import it.polimi.ingsw.view.ModelUpdater;

public class HandChooseObjectiveUpdate extends PlayerTypeUpdate{
    private final String chosenObjectiveId;
    public HandChooseObjectiveUpdate(ModelUpdater modelUpdater, String nickname, String chosenObjectiveId) {
        super(modelUpdater, nickname);
        this.chosenObjectiveId = chosenObjectiveId;
    }

    @Override
    public void update() {
        modelUpdater.playerHandChooseObject(nickname, chosenObjectiveId);
    }
}
