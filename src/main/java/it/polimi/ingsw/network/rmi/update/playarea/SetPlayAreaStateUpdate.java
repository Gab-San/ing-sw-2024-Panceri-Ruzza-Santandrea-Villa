package it.polimi.ingsw.network.rmi.update.playarea;

import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.CardPosition;
import it.polimi.ingsw.SerializableCorner;
import it.polimi.ingsw.network.rmi.update.player.PlayerTypeUpdate;
import it.polimi.ingsw.view.ModelUpdater;

import java.util.List;
import java.util.Map;

/**
 * This class represents a rmi update.
 * <p>
 *     Notifies about the state of the stated player's play area.
 * </p>
 */
public class SetPlayAreaStateUpdate extends PlayerTypeUpdate {
    private final List<CardPosition> cardPositionList;
    private final Map<GameResource, Integer> visibleResources;
    private final List<SerializableCorner> freeCorners;

    /**
     * Constructs the update.
     * @param modelUpdater instance of the model updater referenced to execute updates
     * @param nickname unique id of the player
     * @param cardPositionList play area's matrix represented as a list of card positions
     * @param visibleResources visible resources
     * @param freeCorners play area's free corners
     */
    public SetPlayAreaStateUpdate(ModelUpdater modelUpdater, String nickname, List<CardPosition> cardPositionList, Map<GameResource, Integer> visibleResources, List<SerializableCorner> freeCorners) {
        super(modelUpdater, nickname);
        this.cardPositionList = cardPositionList;
        this.visibleResources = visibleResources;
        this.freeCorners = freeCorners;
    }

    /**
     * Updates the view setting the initial state of the play area.
     */
    @Override
    public void update() {
        modelUpdater.setPlayAreaState(nickname, cardPositionList, visibleResources, freeCorners);
    }
}
