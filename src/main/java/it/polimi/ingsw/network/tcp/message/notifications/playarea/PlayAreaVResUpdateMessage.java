package it.polimi.ingsw.network.tcp.message.notifications.playarea;

import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.tcp.message.notifications.player.PlayerMessage;

import java.io.Serial;
import java.rmi.RemoteException;
import java.util.Map;

/**
 * This class implements tcp server message interface.
 * Notifies about an update in the visible resources list.
 */
public class PlayAreaVResUpdateMessage extends PlayerMessage {
    @Serial
    private static final long serialVersionUID = -5619278361982L;
    private final Map<GameResource, Integer> visibleResources;

    /**
     * Constructs the visible resources update message.
     * @param nickname play area's owner's id
     * @param visibleResources map of currently visible resources
     */
    public PlayAreaVResUpdateMessage(String nickname, Map<GameResource, Integer> visibleResources) {
        super(nickname);
        this.visibleResources = visibleResources;
    }

    @Override
    public void execute(VirtualClient virtualClient) throws RemoteException {
        virtualClient.visibleResourcesUpdate(nickname, visibleResources);
    }
}
