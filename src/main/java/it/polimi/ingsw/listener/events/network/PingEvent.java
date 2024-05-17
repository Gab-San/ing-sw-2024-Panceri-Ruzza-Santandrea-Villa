package it.polimi.ingsw.listener.events.network;

import it.polimi.ingsw.server.VirtualClient;

import java.rmi.RemoteException;

public class PingEvent implements NetworkEvent{
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.ping();
    }
}
