package it.polimi.ingsw.model.listener.remote.events.player;

import it.polimi.ingsw.model.listener.remote.events.UpdateEvent;
import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

/**
 * This class represents a player event.
 * <p>
 *     A player event is an event triggered when a modification involving
 *     a player status and board occurs.
 * </p>
 */
abstract public class PlayerEvent implements UpdateEvent {
    /**
     * Player identifier.
     */
    protected final String nickname;

    /**
     * Constructs a player event.
     * @param nickname involved player's id
     */
    protected PlayerEvent(String nickname) {
        this.nickname = nickname;
    }

    @Override
    abstract public void executeEvent(VirtualClient virtualClient) throws RemoteException;
}
