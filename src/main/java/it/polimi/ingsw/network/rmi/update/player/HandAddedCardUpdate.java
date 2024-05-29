package it.polimi.ingsw.network.rmi.update.player;

import it.polimi.ingsw.view.ModelUpdater;

public class HandAddedCardUpdate extends PlayerTypeUpdate{
    private final String drawnCardId;
    public HandAddedCardUpdate(ModelUpdater modelUpdater, String nickname, String drawnCardId) {
        super(modelUpdater, nickname);
        this.drawnCardId = drawnCardId;
    }

    @Override
    public void update() {
        modelUpdater.playerHandAddedCardUpdate(nickname, drawnCardId);
    }
}
