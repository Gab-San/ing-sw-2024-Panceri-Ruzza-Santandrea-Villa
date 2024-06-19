package it.polimi.ingsw.model.listener.remote.events.board;

import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.model.listener.remote.events.UpdateEvent;
import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

/**
 * This class represents an update event. It is triggered when the phase is changed.
 */
public class ChangePhaseEvent implements UpdateEvent {
    private final GamePhase gamePhase;

    /**
     * Constructs the game phase event.
     * @param gamePhase updated gamephase
     */
    public ChangePhaseEvent(GamePhase gamePhase) {
        this.gamePhase = gamePhase;
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.updatePhase(gamePhase);
    }
}
