package it.polimi.ingsw.model.listener.remote.events.player;

import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

/**
 * This class represents a player removal event. It notifies about a player being removed from the lobby.
 * <p>
 *     A player can be removed due to direct or indirect disconnection during the lobby setup state.
 * </p>
 */
public class PlayerRemovalEvent extends PlayerEvent{
    /**
     * Constructs player removal event.
     * @param nickname removed player's id
     */
    public PlayerRemovalEvent(String nickname) {
        super(nickname);
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.removePlayer(nickname);
    }
}
