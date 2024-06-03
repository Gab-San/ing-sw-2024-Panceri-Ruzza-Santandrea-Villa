package it.polimi.ingsw.network.rmi.update.player;

import it.polimi.ingsw.network.rmi.update.RMIUpdate;
import it.polimi.ingsw.view.ModelUpdater;

/**
 * This class provides the constructor for all the updates dealing with a player.
 */
abstract public class PlayerTypeUpdate extends RMIUpdate {
    /**
     * Unique id of the user.
     */
    protected final String nickname;

    /**
     * Constructs the update.
     * @param modelUpdater instance of the model updater referenced to execute updates
     * @param nickname unique id of the updated user
     */
    protected PlayerTypeUpdate(ModelUpdater modelUpdater, String nickname) {
        super(modelUpdater);
        this.nickname = nickname;
    }
}
