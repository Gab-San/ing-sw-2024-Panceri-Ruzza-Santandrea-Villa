package it.polimi.ingsw.model.listener.remote.events.board;

import it.polimi.ingsw.model.listener.remote.NetworkEvent;
import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

public class EndgameCheckEvent implements NetworkEvent {
    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {

    }
}
