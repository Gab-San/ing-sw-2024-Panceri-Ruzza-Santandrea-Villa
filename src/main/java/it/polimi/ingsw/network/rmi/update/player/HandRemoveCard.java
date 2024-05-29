package it.polimi.ingsw.network.rmi.update.player;

import it.polimi.ingsw.view.ModelUpdater;

public class HandRemoveCard extends PlayerTypeUpdate{
    private final String removedCard;
    public HandRemoveCard(ModelUpdater modelUpdater, String nickname, String removedCard) {
        super(modelUpdater, nickname);
        this.removedCard = removedCard;
    }

    @Override
    public void update() {
        modelUpdater.playerHandRemoveCard(nickname, removedCard);
    }
}
