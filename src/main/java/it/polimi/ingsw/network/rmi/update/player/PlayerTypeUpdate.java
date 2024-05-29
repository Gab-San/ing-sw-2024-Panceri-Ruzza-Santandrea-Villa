package it.polimi.ingsw.network.rmi.update.player;

import it.polimi.ingsw.network.rmi.update.RMIUpdate;
import it.polimi.ingsw.view.ModelUpdater;

abstract public class PlayerTypeUpdate extends RMIUpdate {
    protected final String nickname;
    protected PlayerTypeUpdate(ModelUpdater modelUpdater, String nickname) {
        super(modelUpdater);
        this.nickname = nickname;
    }
}
