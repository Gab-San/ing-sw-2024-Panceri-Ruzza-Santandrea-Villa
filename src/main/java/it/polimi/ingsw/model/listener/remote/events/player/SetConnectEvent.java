package it.polimi.ingsw.model.listener.remote.events.player;

import it.polimi.ingsw.server.VirtualClient;

import java.rmi.RemoteException;

public class SetConnectEvent extends PlayerEvent{
    private final boolean isConnected;
    public SetConnectEvent(String nickname, boolean isConnected) {
        super(nickname);
        this.isConnected = isConnected;
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.updatePlayer(nickname, isConnected);
    }
}
