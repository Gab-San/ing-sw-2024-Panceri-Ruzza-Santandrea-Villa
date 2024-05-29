package it.polimi.ingsw.network.rmi.update.playarea;

import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.model.listener.remote.events.playarea.CardPosition;
import it.polimi.ingsw.model.listener.remote.events.playarea.SerializableCorner;
import it.polimi.ingsw.network.rmi.update.player.PlayerTypeUpdate;
import it.polimi.ingsw.view.ModelUpdater;

import java.util.List;
import java.util.Map;

public class SetPlayAreaStateUpdate extends PlayerTypeUpdate {
    private final List<CardPosition> cardPositionList;
    private final Map<GameResource, Integer> visibleResources;
    private final List<SerializableCorner> freeCorners;
    public SetPlayAreaStateUpdate(ModelUpdater modelUpdater, String nickname, List<CardPosition> cardPositionList, Map<GameResource, Integer> visibleResources, List<SerializableCorner> freeCorners) {
        super(modelUpdater, nickname);
        this.cardPositionList = cardPositionList;
        this.visibleResources = visibleResources;
        this.freeCorners = freeCorners;
    }

    @Override
    public void update() {
        modelUpdater.setPlayAreaState(nickname, cardPositionList, visibleResources, freeCorners);
    }
}
