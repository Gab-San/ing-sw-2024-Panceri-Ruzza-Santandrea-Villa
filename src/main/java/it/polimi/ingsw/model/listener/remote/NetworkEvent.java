package it.polimi.ingsw.model.listener.remote;

import it.polimi.ingsw.model.listener.GameEvent;
import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

/**
 * This interface extends game event and structures a network event.
 *
 * <p>
 *     Networks event are event that are to be sent over
 *     a network.
 * </p>
 */
public interface NetworkEvent extends GameEvent {
    void executeEvent(VirtualClient virtualClient) throws RemoteException;
}
