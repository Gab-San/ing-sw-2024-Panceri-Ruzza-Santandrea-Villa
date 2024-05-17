package it.polimi.ingsw.listener.remote.events;

import it.polimi.ingsw.listener.GameEvent;
import it.polimi.ingsw.server.VirtualClient;

import java.rmi.RemoteException;

public interface NetworkEvent extends GameEvent {
    void executeEvent(VirtualClient virtualClient) throws RemoteException;
}
