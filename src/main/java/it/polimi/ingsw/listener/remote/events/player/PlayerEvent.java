package it.polimi.ingsw.listener.remote.events.player;

import it.polimi.ingsw.listener.remote.events.NetworkEvent;
import it.polimi.ingsw.server.VirtualClient;

import java.rmi.RemoteException;

abstract public class PlayerEvent implements NetworkEvent {
    protected final String nickname;

    public PlayerEvent(String nickname) {
        this.nickname = nickname;
    }

    @Override
    abstract public void executeEvent(VirtualClient virtualClient) throws RemoteException;
}
