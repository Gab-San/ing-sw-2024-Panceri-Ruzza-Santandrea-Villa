package it.polimi.ingsw.model.listener.remote.events.player;

import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

/**
 * This class represents a player event. It is triggered when the player's color is chosen.
 */
public class SetColorEvent extends PlayerEvent {
    private final PlayerColor color;

    /**
     * Constructs the set color event.
     * @param nickname player's id
     * @param color chosen color
     */
    public SetColorEvent(String nickname, PlayerColor color) {
        super(nickname);
        this.color = color;
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.updatePlayer(nickname, color);
    }
}
