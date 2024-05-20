package it.polimi.ingsw.model.listener.remote.events.player;

import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

public class SetTurnEvent extends PlayerEvent {
    private final int playerTurn;

    public SetTurnEvent(String nickname, int playerTurn) {
        super(nickname);
        this.playerTurn = playerTurn;
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.updatePlayer(nickname, playerTurn);
    }
}
