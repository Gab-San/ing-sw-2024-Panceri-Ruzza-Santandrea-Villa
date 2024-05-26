package it.polimi.ingsw.model.listener.remote.events.player;

import it.polimi.ingsw.model.enums.PlayerColor;
import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

public class PlayerStateUpdateEvent extends PlayerEvent {
    private final boolean isConnected;
    private final int turn;
    private final PlayerColor colour;

    public PlayerStateUpdateEvent(String nickname, boolean isConnected, int turn, PlayerColor colour){
        super(nickname);
        this.isConnected = isConnected;
        this.turn = turn;
        this.colour = colour;
    }
    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.setPlayerState(nickname, isConnected, turn, colour);
    }
}
