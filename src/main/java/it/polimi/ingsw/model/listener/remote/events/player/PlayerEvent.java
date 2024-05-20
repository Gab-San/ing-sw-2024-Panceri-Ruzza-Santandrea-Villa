package it.polimi.ingsw.model.listener.remote.events.player;

import it.polimi.ingsw.model.listener.remote.NetworkEvent;
import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

abstract public class PlayerEvent implements NetworkEvent {
    protected final String nickname;

    protected PlayerEvent(String nickname) {
        this.nickname = nickname;
    }

    @Override
    abstract public void executeEvent(VirtualClient virtualClient) throws RemoteException;
}
