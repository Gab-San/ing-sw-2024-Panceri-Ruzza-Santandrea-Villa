package it.polimi.ingsw.network.tcp.message.notifications.player;

import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.network.VirtualClient;

import java.io.Serial;
import java.rmi.RemoteException;

/**
 * This class is a player message. Notifies of a player's color choice.
 */
public class SetColorMessage extends PlayerMessage {
    @Serial
    private static final long serialVersionUID = 1476857619273L;
    private final PlayerColor color;

    /**
     * Constructs the color message.
     * @param nickname player's unique id
     * @param color player's chosen color
     */
    public SetColorMessage(String nickname, PlayerColor color) {
        super(nickname);
        this.color = color;
    }

    @Override
    public void execute(VirtualClient virtualClient) throws RemoteException {
        virtualClient.updatePlayer(nickname, color);
    }
}
