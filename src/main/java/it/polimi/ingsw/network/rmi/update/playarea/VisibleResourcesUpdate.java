package it.polimi.ingsw.network.rmi.update.playarea;

import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.network.rmi.update.player.PlayerTypeUpdate;
import it.polimi.ingsw.view.ModelUpdater;

import java.util.Map;

/**
 * This class represents a rmi update.
 * <p>
 *     Notifies about a change in the visible resources.
 * </p>
 */
public class VisibleResourcesUpdate extends PlayerTypeUpdate {
    private final Map<GameResource, Integer> visibleResources;

    /**
     * Constructs the update.
     * @param modelUpdater instance of the model updater referenced to execute updates
     * @param nickname unique id of the player
     * @param visibleResources visible resources on the specified player's play area
     */
    public VisibleResourcesUpdate(ModelUpdater modelUpdater, String nickname, Map<GameResource, Integer> visibleResources) {
        super(modelUpdater, nickname);
        this.visibleResources = visibleResources;
    }

    /**
     * Updates the value of the visible resources on the view.
     */
    @Override
    public void update() {
        modelUpdater.visibleResourcesUpdate(nickname, visibleResources);
    }
}
