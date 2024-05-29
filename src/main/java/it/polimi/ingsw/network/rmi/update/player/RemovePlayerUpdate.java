package it.polimi.ingsw.network.rmi.update.player;

import it.polimi.ingsw.view.ModelUpdater;

public class RemovePlayerUpdate extends PlayerTypeUpdate{
    public RemovePlayerUpdate(ModelUpdater modelUpdater, String nickname) {
        super(modelUpdater, nickname);
    }

    @Override
    public void update() {
        modelUpdater.removePlayer(nickname);
    }
}
