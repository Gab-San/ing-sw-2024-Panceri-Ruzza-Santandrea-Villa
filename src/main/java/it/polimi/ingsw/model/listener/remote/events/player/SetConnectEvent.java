package it.polimi.ingsw.model.listener.remote.events.player;

import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;
/**
 * This class represents a player event. It is triggered when a player's connection status changes.
 */
public class SetConnectEvent extends PlayerEvent{
    private final boolean isConnected;

    /**
     * Constructs set connect event.
     * @param nickname player's id
     * @param isConnected true if the player is connected, false otherwise.
     */
    public SetConnectEvent(String nickname, boolean isConnected) {
        super(nickname);
        this.isConnected = isConnected;
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.updatePlayer(nickname, isConnected);
    }
}
