package it.polimi.ingsw.listener.remote.events;

import it.polimi.ingsw.listener.remote.events.NetworkEvent;
import it.polimi.ingsw.server.VirtualClient;

import java.rmi.RemoteException;

public class PingEvent implements NetworkEvent {
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.ping();
    }
}
