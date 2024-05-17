package it.polimi.ingsw.listener.events.network.player;

import it.polimi.ingsw.listener.events.network.NetworkEvent;
import it.polimi.ingsw.server.VirtualClient;

import java.rmi.RemoteException;

public class PlayerEvent implements NetworkEvent {
    protected final String nickname;

    public PlayerEvent(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.updatePlayer(nickname);
    }
}
