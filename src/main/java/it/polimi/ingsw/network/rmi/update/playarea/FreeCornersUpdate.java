package it.polimi.ingsw.network.rmi.update.playarea;

import it.polimi.ingsw.model.listener.remote.events.playarea.SerializableCorner;
import it.polimi.ingsw.network.rmi.update.player.PlayerTypeUpdate;
import it.polimi.ingsw.view.ModelUpdater;

import java.util.List;

public class FreeCornersUpdate extends PlayerTypeUpdate {
    private final List<SerializableCorner> freeCorners;
    public FreeCornersUpdate(ModelUpdater modelUpdater, String nickname, List<SerializableCorner> freeCorners) {
        super(modelUpdater, nickname);
        this.freeCorners = freeCorners;
    }

    @Override
    public void update() {
        modelUpdater.freeCornersUpdate(nickname, freeCorners);
    }
}
