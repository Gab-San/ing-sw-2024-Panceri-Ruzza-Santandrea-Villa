package it.polimi.ingsw.model.listener.remote.events.board;

import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.listener.remote.events.NetworkEvent;
import it.polimi.ingsw.server.VirtualClient;

import java.rmi.RemoteException;

public class ChangePhaseEvent implements NetworkEvent {
    private final GamePhase gamePhase;

    public ChangePhaseEvent(GamePhase gamePhase) {
        this.gamePhase = gamePhase;
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.updatePhase(gamePhase);
    }
}
