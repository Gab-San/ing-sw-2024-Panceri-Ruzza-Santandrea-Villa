package it.polimi.ingsw.model.listener.remote.events.player;

import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

public class SetColorEvent extends PlayerEvent {
    private final PlayerColor color;

    public SetColorEvent(String nickname, PlayerColor color) {
        super(nickname);
        this.color = color;
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.updatePlayer(nickname, color);
    }

    @Override
    public String toString() {
        return  super.toString() +
                "\nSetColorEvent{" +
                "color=" + color +
                '}';
    }
}
