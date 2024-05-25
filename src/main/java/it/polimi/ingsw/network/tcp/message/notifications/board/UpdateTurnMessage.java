package it.polimi.ingsw.network.tcp.message.notifications.board;

import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.tcp.message.TCPServerMessage;

import java.rmi.RemoteException;

public class UpdateTurnMessage implements TCPServerMessage {
    private final int currentTurn;
    public UpdateTurnMessage(int currentTurn) {
        this.currentTurn = currentTurn;
    }

    @Override
    public boolean isCheck() {
        return false;
    }

    @Override
    public void execute(VirtualClient virtualClient) throws RemoteException {
        virtualClient.updateTurn(currentTurn);
    }
}
