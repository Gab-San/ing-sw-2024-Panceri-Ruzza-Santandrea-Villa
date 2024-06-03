package it.polimi.ingsw.network.rmi.update.player;

import it.polimi.ingsw.view.ModelUpdater;

/**
 * This class represents a rmi update.
 * <p>
 *     Notifies about player removal.
 * </p>
 */
public class RemovePlayerUpdate extends PlayerTypeUpdate{
    /**
     * Constructs the update.
     * @param modelUpdater instance of the model updater referenced to execute updates
     * @param nickname unique id of the user who was removed
     */
    public RemovePlayerUpdate(ModelUpdater modelUpdater, String nickname) {
        super(modelUpdater, nickname);
    }

    /**
     * Updates the view notifying about a player leaving the table, before the start of the game.
     */
    @Override
    public void update() {
        modelUpdater.removePlayer(nickname);
    }
}
