package it.polimi.ingsw.network.tcp.message.notifications.player;

import it.polimi.ingsw.network.VirtualClient;

import java.io.Serial;
import java.rmi.RemoteException;

public class SetTurnMessage extends PlayerMessage {
    @Serial
    private static final long serialVersionUID = 198047120983L;
    private final int playerTurn;
    public SetTurnMessage(String nickname, int playerTurn) {
        super(nickname);
        this.playerTurn = playerTurn;
    }

    @Override
    public void execute(VirtualClient virtualClient) throws RemoteException {
        virtualClient.updatePlayer(nickname, playerTurn);
    }
}
