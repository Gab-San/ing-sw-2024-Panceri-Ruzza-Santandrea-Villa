package it.polimi.ingsw.network.rmi.update.player;

import it.polimi.ingsw.view.ModelUpdater;

public class HandSetStartingCard extends PlayerTypeUpdate{
    private final String startingCardId;
    public HandSetStartingCard(ModelUpdater modelUpdater, String nickname, String startingCardId) {
        super(modelUpdater, nickname);
        this.startingCardId = startingCardId;
    }

    @Override
    public void update() {
        modelUpdater.playerHandSetStartingCard(nickname, startingCardId);
    }
}
