package it.polimi.ingsw.model.listener.remote.events.player;

import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;
/**
 * This class represents a player event. It is triggered when the player's turn is assigned.
 */
public class SetTurnEvent extends PlayerEvent {
    private final int playerTurn;

    /**
     * Constructs set turn event.
     * @param nickname player's id
     * @param playerTurn assigned turn
     */
    public SetTurnEvent(String nickname, int playerTurn) {
        super(nickname);
        this.playerTurn = playerTurn;
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.updatePlayer(nickname, playerTurn);
    }
}
