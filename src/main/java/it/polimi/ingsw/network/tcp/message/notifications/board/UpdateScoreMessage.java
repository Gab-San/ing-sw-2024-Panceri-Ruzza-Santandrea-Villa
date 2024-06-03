package it.polimi.ingsw.network.tcp.message.notifications.board;

import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.tcp.message.TCPServerMessage;

import java.io.Serial;
import java.rmi.RemoteException;

/**
 * This class implements tcp server message interface.
 * Used to notify player score change.
 */
public class UpdateScoreMessage implements TCPServerMessage {
    @Serial
    private static final long serialVersionUID = 12874192847L;
    private final String nickname;
    private final int score;

    /**
     * Constructs score message.
     * @param nickname unique identifier of the player whose score has changed
     * @param score updated player's score
     */
    public UpdateScoreMessage(String nickname, int score) {
        this.nickname = nickname;
        this.score = score;
    }

    @Override
    public boolean isCheck() {
        return false;
    }

    @Override
    public void execute(VirtualClient virtualClient) throws RemoteException {
        virtualClient.updateScore(nickname,score);
    }
}
