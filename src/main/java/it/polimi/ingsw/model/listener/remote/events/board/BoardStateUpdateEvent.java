package it.polimi.ingsw.model.listener.remote.events.board;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.model.listener.remote.events.UpdateEvent;
import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a board state event. Notifies about the board state.
 */
public class BoardStateUpdateEvent implements UpdateEvent {
    private final int currentTurn;
    private final Map<String, Integer> scoreboard;
    private final GamePhase gamePhase;
    private final Map<String, Boolean> playerDeadLock;

    /**
     * Constructs the board state update event.
     * @param currentTurn current turn
     * @param gameScoreboard map of the player's scores
     * @param gamePhase current game phase
     * @param isPlayerDeadLocked map of player deadlocks
     */
    public BoardStateUpdateEvent(int currentTurn, Map<Player, Integer> gameScoreboard, GamePhase gamePhase, Map<Player, Boolean> isPlayerDeadLocked) {
        this.currentTurn = currentTurn;
        this.scoreboard = new HashMap<>();
        for(Player p: gameScoreboard.keySet()){
            this.scoreboard.put(p.getNickname(), gameScoreboard.get(p));
        }
        this.gamePhase = gamePhase;
        this.playerDeadLock = new HashMap<>();
        for(Player p : isPlayerDeadLocked.keySet()){
            playerDeadLock.put(p.getNickname(), isPlayerDeadLocked.get(p));
        }
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.setBoardState(currentTurn, scoreboard, gamePhase, playerDeadLock);
    }
}
