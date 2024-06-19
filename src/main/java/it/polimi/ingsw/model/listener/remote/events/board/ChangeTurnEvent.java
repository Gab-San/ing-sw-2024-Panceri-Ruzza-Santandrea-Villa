package it.polimi.ingsw.model.listener.remote.events.board;

import it.polimi.ingsw.model.listener.remote.events.UpdateEvent;
import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

/**
 * This class represents an update event. Notifies about a turn change.
 */
public class ChangeTurnEvent implements UpdateEvent {
    private final int currentTurn;

    /**
     * Constructs the change turn event.
     * @param currentTurn updated turn
     */
    public ChangeTurnEvent(int currentTurn) {
        this.currentTurn = currentTurn;
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.updateTurn(currentTurn);
    }
}
