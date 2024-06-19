package it.polimi.ingsw.model.listener.remote.events.board;

import it.polimi.ingsw.model.listener.remote.events.UpdateEvent;
import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

/**
 * This class represents an update event. It notifies about a change in the score.
 */
public class ChangeScoreEvent implements UpdateEvent {
    private final String nickname;
    private final int score;
    /**
     * Constructs the score event.
     * @param nickname player's id whose score has changed
     * @param score updated player's score
     */
    public ChangeScoreEvent(String nickname, int score) {
        this.nickname = nickname;
        this.score = score;
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.updateScore(nickname, score);
    }
}
