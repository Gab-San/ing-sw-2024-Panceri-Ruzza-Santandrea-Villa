package it.polimi.ingsw.network.tcp.message.notifications.player;

import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.network.VirtualClient;

import java.io.Serial;
import java.rmi.RemoteException;

/**
 * This class inherits from player message. Notifies of a player state.
 */
public class PlayerStateMessage extends PlayerMessage {
    @Serial
    private static final long serialVersionUID = 184976587162L;
    private final boolean isConnected;
    private final int turn;
    private final PlayerColor colour;

    /**
     * Constructs the player state message.
     * @param nickname player's unique id
     * @param isConnected true if the player is connected, false otherwise
     * @param turn player's turn
     * @param colour player's color
     */
    public PlayerStateMessage(String nickname, boolean isConnected, int turn, PlayerColor colour){
        super(nickname);
        this.isConnected = isConnected;
        this.turn = turn;
        this.colour = colour;
    }


    @Override
    public void execute(VirtualClient virtualClient) throws RemoteException {
        virtualClient.setPlayerState(nickname, isConnected, turn, colour);
    }
}
