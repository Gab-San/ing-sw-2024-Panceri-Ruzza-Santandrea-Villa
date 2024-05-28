package it.polimi.ingsw.network.tcp.message.notifications.playarea;

import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.tcp.message.notifications.player.PlayerMessage;

import java.io.Serial;
import java.rmi.RemoteException;
import java.util.Map;

public class PlayAreaVResUpdateMessage extends PlayerMessage {
    @Serial
    private static final long serialVersionUID = -5619278361982L;
    private final Map<GameResource, Integer> visibleResources;
    public PlayAreaVResUpdateMessage(String nickname, Map<GameResource, Integer> visibleResources) {
        super(nickname);
        this.visibleResources = visibleResources;
    }

    @Override
    public void execute(VirtualClient virtualClient) throws RemoteException {
        virtualClient.visibleResourcesUpdate(nickname, visibleResources);
    }
}
