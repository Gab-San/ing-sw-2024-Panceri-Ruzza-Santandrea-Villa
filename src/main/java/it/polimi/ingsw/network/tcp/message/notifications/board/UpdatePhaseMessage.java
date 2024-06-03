package it.polimi.ingsw.network.tcp.message.notifications.board;

import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.tcp.message.TCPServerMessage;

import java.io.Serial;
import java.rmi.RemoteException;

/**
 * This class implements tcp server message interface.
 * Used to notify phase change.
 */
public class UpdatePhaseMessage implements TCPServerMessage {
    @Serial
    private static final long serialVersionUID = 1274985L;
    private final GamePhase gamePhase;

    /**
     * Constructs the update phase message.
     * @param gamePhase current game phase
     */
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
