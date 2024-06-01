package it.polimi.ingsw.model.listener.remote.events.board;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.model.listener.remote.events.UpdateEvent;
import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class BoardStateUpdateEvent implements UpdateEvent {
    private final int currentTurn;
    private static int updateNumber;
    private final Map<String, Integer> scoreboard;
    private final GamePhase gamePhase;
    private final Map<String, Boolean> playerDeadLock;

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
        updateNumber++;
        virtualClient.setBoardState(currentTurn, scoreboard, gamePhase, playerDeadLock);
    }

    @Override
    public String toString() {
        return "Board State Update " + updateNumber + " [GAME PHASE] " + gamePhase;
    }
}
