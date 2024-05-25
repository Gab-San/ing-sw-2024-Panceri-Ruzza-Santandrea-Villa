package it.polimi.ingsw.model.listener.remote.errors;

import it.polimi.ingsw.model.listener.remote.events.player.PlayerEvent;
import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

public class TimeoutDisconnectEvent extends RemoteErrorEvent {
    public TimeoutDisconnectEvent(String nickname) {
        super(nickname);
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.notifyTimeoutDisconnect();
    }
}
