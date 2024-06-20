package it.polimi.ingsw.model.listener.remote.events.player;

import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

/**
 * This class represents a player event. Notifies about the current status of a player.
 */
public class PlayerStateUpdateEvent extends PlayerEvent {
    private final boolean isConnected;
    private final int turn;
    private final PlayerColor colour;

    /**
     * Constructs the player state event.
     * @param nickname player's id
     * @param isConnected true if the player is currently connected, false otherwise
     * @param turn player's turn
     * @param colour player's color
     */
    public PlayerStateUpdateEvent(String nickname, boolean isConnected, int turn, PlayerColor colour){
        super(nickname);
        this.isConnected = isConnected;
        this.turn = turn;
        this.colour = colour;
    }
    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.setPlayerState(nickname, isConnected, turn, colour);
    }
}
