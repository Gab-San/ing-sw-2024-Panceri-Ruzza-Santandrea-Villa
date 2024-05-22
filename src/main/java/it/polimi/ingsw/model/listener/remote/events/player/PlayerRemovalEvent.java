package it.polimi.ingsw.model.listener.remote.events.player;

import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

public class PlayerRemovalEvent extends PlayerEvent{
    public PlayerRemovalEvent(String nickname) {
        super(nickname);
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.removePlayer(nickname);
    }
}
