package it.polimi.ingsw.model.listener.remote.events.board;

import it.polimi.ingsw.model.listener.remote.events.NetworkEvent;
import it.polimi.ingsw.server.VirtualClient;

import java.rmi.RemoteException;

public class ChangeTurnEvent implements NetworkEvent {
    private final int currentTurn;

    public ChangeTurnEvent(int currentTurn) {
        this.currentTurn = currentTurn;
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.updateTurn(currentTurn);
    }
}
