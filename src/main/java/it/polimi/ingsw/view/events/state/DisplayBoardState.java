package it.polimi.ingsw.view.events.state;

import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.view.events.TUIEvent;
import it.polimi.ingsw.view.tui.TUI;

import java.util.Map;

/**
 * This class handles events triggered by board state update.
 */
public class DisplayBoardState implements TUIEvent {
    private final int currentTurn;
    private final Map<String, Integer> scoreboard;
    private final GamePhase gamePhase;
    private final Map<String, Boolean> playersDeadLock;

    /**
     * Constructs a board state event.
     * @param currentTurn current match's turn.
     * @param scoreboard scoreboard
     * @param gamePhase current game phase
     * @param playersDeadLock map of players deadlock
     */
    public DisplayBoardState(int currentTurn, Map<String, Integer> scoreboard,
                             GamePhase gamePhase, Map<String, Boolean> playersDeadLock) {
        this.currentTurn = currentTurn;
        this.scoreboard = scoreboard;
        this.gamePhase = gamePhase;
        this.playersDeadLock = playersDeadLock;
    }

    @Override
    public void displayEvent(TUI tui) {
        tui.showNotification("Board initialised at " + currentTurn + "\nwith: " + scoreboard +
                "\nGAME PHASE: " + gamePhase + "\nPLAYER DEAD LOCK: " + playersDeadLock);
    }
}
