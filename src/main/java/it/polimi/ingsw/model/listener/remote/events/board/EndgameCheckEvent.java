package it.polimi.ingsw.model.listener.remote.events.board;

import it.polimi.ingsw.model.listener.remote.events.NetworkEvent;
import it.polimi.ingsw.server.VirtualClient;

import java.rmi.RemoteException;

public class EndgameCheckEvent implements NetworkEvent {
    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {

    }
}
