package it.polimi.ingsw.network.tcp.message.notifications.player;

import it.polimi.ingsw.network.VirtualClient;

import java.io.Serial;
import java.rmi.RemoteException;

/**
 * This class represents a player message. Carries information about the turn of the player.
 */
public class SetTurnMessage extends PlayerMessage {
    @Serial
    private static final long serialVersionUID = 198047120983L;
    private final int playerTurn;

    /**
     * Constructs the turn message.
     * @param nickname player's unique id
     * @param playerTurn player's assigned turn
     */
    public SetTurnMessage(String nickname, int playerTurn) {
        super(nickname);
        this.playerTurn = playerTurn;
    }

    @Override
    public void execute(VirtualClient virtualClient) throws RemoteException {
        virtualClient.updatePlayer(nickname, playerTurn);
    }
}
