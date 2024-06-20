package it.polimi.ingsw.model.listener.remote.events.playarea;

import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.model.listener.remote.events.player.PlayerEvent;
import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;
import java.util.Map;

/**
 * This class represents a player event. It notifies about an update of the visible resources.
 */
public class VisibleResourcesUpdateEvent extends PlayerEvent {
    private final Map<GameResource, Integer> visibleResources;

    /**
     * Constructs the visible resources update event.
     * @param nickname play area owner's id
     * @param visibleResources current visible updates
     */
    public VisibleResourcesUpdateEvent(String nickname, Map<GameResource, Integer> visibleResources) {
        super(nickname);
        this.visibleResources = visibleResources;
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.visibleResourcesUpdate(nickname, visibleResources);
    }
}
