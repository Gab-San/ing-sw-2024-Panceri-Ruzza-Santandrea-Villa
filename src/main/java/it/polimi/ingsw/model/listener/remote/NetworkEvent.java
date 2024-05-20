package it.polimi.ingsw.model.listener.remote;

import it.polimi.ingsw.model.listener.GameEvent;
import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

public interface NetworkEvent extends GameEvent {
    void executeEvent(VirtualClient virtualClient) throws RemoteException;
}
