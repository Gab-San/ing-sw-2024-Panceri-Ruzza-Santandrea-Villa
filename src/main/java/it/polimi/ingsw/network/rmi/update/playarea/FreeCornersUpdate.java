package it.polimi.ingsw.network.rmi.update.playarea;

import it.polimi.ingsw.model.listener.remote.events.playarea.SerializableCorner;
import it.polimi.ingsw.network.rmi.update.player.PlayerTypeUpdate;
import it.polimi.ingsw.view.ModelUpdater;

import java.util.List;

/**
 * This class represents a rmi update.
 * <p>
 *     Notifies about a change in the free corners' list.
 * </p>
 */
public class FreeCornersUpdate extends PlayerTypeUpdate {
    private final List<SerializableCorner> freeCorners;

    /**
     * Constructs the update.
     * @param modelUpdater instance of the model updater referenced to execute updates
     * @param nickname unique id of the player
     * @param freeCorners list of free corners
     */
    public FreeCornersUpdate(ModelUpdater modelUpdater, String nickname, List<SerializableCorner> freeCorners) {
        super(modelUpdater, nickname);
        this.freeCorners = freeCorners;
    }

    /**
     * Updates the view showing the updated list of free corners
     */
    @Override
    public void update() {
        modelUpdater.freeCornersUpdate(nickname, freeCorners);
    }
}
