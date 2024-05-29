package it.polimi.ingsw.network.rmi.update.playarea;

import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.network.rmi.update.player.PlayerTypeUpdate;
import it.polimi.ingsw.view.ModelUpdater;

import java.util.Map;

public class VisibleResourcesUpdate extends PlayerTypeUpdate {
    private final Map<GameResource, Integer> visibleResources;
    public VisibleResourcesUpdate(ModelUpdater modelUpdater, String nickname, Map<GameResource, Integer> visibleResources) {
        super(modelUpdater, nickname);
        this.visibleResources = visibleResources;
    }

    @Override
    public void update() {
        modelUpdater.visibleResourcesUpdate(nickname, visibleResources);
    }
}
