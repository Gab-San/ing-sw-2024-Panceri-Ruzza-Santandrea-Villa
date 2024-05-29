package it.polimi.ingsw.network.rmi.update.player;

import it.polimi.ingsw.view.ModelUpdater;

public class PlayerDeadLockUpdate extends PlayerTypeUpdate {
    private final boolean isPlayerDeadLocked;
    public PlayerDeadLockUpdate(ModelUpdater modelUpdater, String nickname, boolean isPlayerDeadLocked) {
        super(modelUpdater, nickname);
        this.isPlayerDeadLocked = isPlayerDeadLocked;
    }

    @Override
    public void update() {
        modelUpdater.playerDeadLockUpdate(nickname, isPlayerDeadLocked);
    }
}
