package it.polimi.ingsw.model.listener.remote.events.board;

import it.polimi.ingsw.model.listener.remote.events.UpdateEvent;
import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

/**
 * This class represents an update event. Notifies about the endgame start.
 */
public class EndgameCheckEvent implements UpdateEvent {
    private final String nickname;
    private final int score;
    private final boolean emptyDecks;

    /**
     * Constructs an endgame event if a player has reached threshold score.
     * @param nickname player's id who reached threshold score
     * @param score score with which the player reached the threshold score
     */
    public EndgameCheckEvent(String nickname, int score) {
        this.nickname = nickname;
        this.score = score;
        this.emptyDecks = false;
    }

    /**
     * Constructs an endgame event if the decks have been depleted.
     */
    public EndgameCheckEvent(){
        this.nickname = null;
        this.score = 0;
        this.emptyDecks = true;
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        if(emptyDecks){
            virtualClient.notifyEndgame();
            return;
        }
        virtualClient.notifyEndgame(nickname, score);
    }
}
