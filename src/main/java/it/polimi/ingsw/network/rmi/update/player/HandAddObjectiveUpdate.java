package it.polimi.ingsw.network.rmi.update.player;

import it.polimi.ingsw.view.ModelUpdater;

public class HandAddObjectiveUpdate extends PlayerTypeUpdate{
    private final String objectiveCard;
    public HandAddObjectiveUpdate(ModelUpdater modelUpdater, String nickname, String objectiveCard) {
        super(modelUpdater, nickname);
        this.objectiveCard = objectiveCard;
    }

    @Override
    public void update() {
        modelUpdater.playerHandAddObjective(nickname, objectiveCard);
    }
}
