package it.polimi.ingsw.network.rmi.update.player;

import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.view.ModelUpdater;

public class SetPlayerStateUpdate extends PlayerTypeUpdate{
    private final boolean isConnected;
    private final int turn;
    private final PlayerColor color;
    public SetPlayerStateUpdate(ModelUpdater modelUpdater, String nickname, boolean isConnected, int turn, PlayerColor color) {
        super(modelUpdater, nickname);
        this.isConnected = isConnected;
        this.turn = turn;
        this.color = color;
    }

    @Override
    public void update() {
        modelUpdater.setPlayerState(nickname, isConnected, turn, color);
    }
}
