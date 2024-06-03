package it.polimi.ingsw.network.tcp.message.notifications.board;

import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.tcp.message.TCPServerMessage;

import java.io.Serial;
import java.rmi.RemoteException;

/**
 * This class implements tcp server message interface.
 * Sent when endgame is reached.
 */
public class EndgameMessage implements TCPServerMessage {
    @Serial
    private static final long serialVersionUID = 198429418L;
    private final String nickname;
    private final int score;
    private final boolean emptyDecks;

    /**
     * Constructor for endgame message if caused by a player reaching or surpassing the endgame score limit.
     * @param nickname unique identifier of the player
     * @param score score with which the player has reached or surpassed the endgame score limit
     */
    public EndgameMessage(String nickname, int score) {
        this.nickname = nickname;
        this.score = score;
        this.emptyDecks = false;
    }

    /**
     * Constructor for endgame message if caused by empty decks.
     */
    public EndgameMessage(){
        this.nickname = null;
        this.score = 0;
        this.emptyDecks = true;
    }

    @Override
    public boolean isCheck() {
        return false;
    }

    @Override
    public void execute(VirtualClient virtualClient) throws RemoteException {
        if(emptyDecks)
            virtualClient.notifyEndgame();
        else virtualClient.notifyEndgame(nickname, score);
    }
}
