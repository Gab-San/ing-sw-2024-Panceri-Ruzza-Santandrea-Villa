package it.polimi.ingsw.network.rmi.update.player;

import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.view.ModelUpdater;

/**
 * This class represents a rmi update.
 *
 * <p>
 *     It notifies about the current state of the specified player.
 * </p>
 */
public class SetPlayerStateUpdate extends PlayerTypeUpdate{
    private final boolean isConnected;
    private final int turn;
    private final PlayerColor color;

    /**
     * Constructs the update.
     * @param modelUpdater instance of the model updater referenced to execute updates
     * @param nickname unique id of the user
     * @param isConnected true if the player is currently connected, false otherwise
     * @param turn turn assigned to the player
     * @param color color bound to the player
     */
    public SetPlayerStateUpdate(ModelUpdater modelUpdater, String nickname, boolean isConnected, int turn, PlayerColor color) {
        super(modelUpdater, nickname);
        this.isConnected = isConnected;
        this.turn = turn;
        this.color = color;
    }

    /**
     * Updates the view initializing the player state.
     */
    @Override
    public void update() {
        modelUpdater.setPlayerState(nickname, isConnected, turn, color);
    }
}
