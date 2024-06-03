package it.polimi.ingsw.model.listener.remote.events.board;

import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.model.listener.remote.events.UpdateEvent;
import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

public class ChangePhaseEvent implements UpdateEvent {
    private final GamePhase gamePhase;

    public ChangePhaseEvent(GamePhase gamePhase) {
        this.gamePhase = gamePhase;
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.updatePhase(gamePhase);
    }
}
