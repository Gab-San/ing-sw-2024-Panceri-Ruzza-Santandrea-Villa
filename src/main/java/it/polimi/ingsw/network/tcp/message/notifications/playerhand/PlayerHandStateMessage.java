package it.polimi.ingsw.network.tcp.message.notifications.playerhand;

import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.tcp.message.notifications.player.PlayerMessage;

import java.rmi.RemoteException;

public class PlayerHandStateMessage extends PlayerMessage {
    public PlayerHandStateMessage(String nickname) {
        super(nickname);
    }

    @Override
    public void execute(VirtualClient virtualClient) throws RemoteException {

    }
}
