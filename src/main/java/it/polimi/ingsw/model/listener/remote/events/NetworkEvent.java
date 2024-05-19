package it.polimi.ingsw.model.listener.remote.events;

import it.polimi.ingsw.model.listener.GameEvent;
import it.polimi.ingsw.server.VirtualClient;

import java.rmi.RemoteException;

public interface NetworkEvent extends GameEvent {
    void executeEvent(VirtualClient virtualClient) throws RemoteException;
}
