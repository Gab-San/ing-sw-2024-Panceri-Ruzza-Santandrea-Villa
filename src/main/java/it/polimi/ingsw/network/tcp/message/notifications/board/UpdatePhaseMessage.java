package it.polimi.ingsw.network.tcp.message.notifications.board;

import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.tcp.message.TCPServerMessage;

import java.io.Serial;
import java.rmi.RemoteException;

public class UpdatePhaseMessage implements TCPServerMessage {
    @Serial
    private static final long serialVersionUID = 1274985L;
    private final GamePhase gamePhase;

    public UpdatePhaseMessage(GamePhase gamePhase) {
        this.gamePhase = gamePhase;
    }

    @Override
    public boolean isCheck() {
        return false;
    }

    @Override
    public void execute(VirtualClient virtualClient) throws RemoteException {
        virtualClient.updatePhase(gamePhase);
    }
}
