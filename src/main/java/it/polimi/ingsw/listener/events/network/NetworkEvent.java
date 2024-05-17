package it.polimi.ingsw.listener.events.network;

import it.polimi.ingsw.listener.events.GameEvent;
import it.polimi.ingsw.server.VirtualClient;

import java.rmi.RemoteException;

public interface NetworkEvent extends GameEvent {
    void executeEvent(VirtualClient virtualClient) throws RemoteException;
}
