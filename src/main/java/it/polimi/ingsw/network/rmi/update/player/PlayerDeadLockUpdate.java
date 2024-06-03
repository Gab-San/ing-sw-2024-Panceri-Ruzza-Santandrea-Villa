package it.polimi.ingsw.network.rmi.update.player;

import it.polimi.ingsw.view.ModelUpdater;

/**
 * This class represents a rmi update.
 * <p>
 *     Notifies of a player being deadlock.
 * </p>
 */
public class PlayerDeadLockUpdate extends PlayerTypeUpdate {
    private final boolean isPlayerDeadLocked;

    /**
     * Constructs the update.
     * @param modelUpdater instance of the model updater referenced to execute updates
     * @param nickname unique id of the player
     * @param isPlayerDeadLocked true if player is deadlocked, false otherwise
     */
    public PlayerDeadLockUpdate(ModelUpdater modelUpdater, String nickname, boolean isPlayerDeadLocked) {
        super(modelUpdater, nickname);
        this.isPlayerDeadLocked = isPlayerDeadLocked;
    }

    /**
     * Updates the view notifying of a player deadlock.
     */
    @Override
    public void update() {
        modelUpdater.playerDeadLockUpdate(nickname, isPlayerDeadLocked);
    }
}
