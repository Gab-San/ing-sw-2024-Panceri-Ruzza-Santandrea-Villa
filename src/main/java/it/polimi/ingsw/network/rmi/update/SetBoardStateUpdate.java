package it.polimi.ingsw.network.rmi.update;

import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.view.ModelUpdater;

import java.util.Map;

/**
 * This class represents a rmi update.
 * <p>
 *  It notifies about the current state of the board.
 * </p>
 */
public class SetBoardStateUpdate extends RMIUpdate {
    private final int currentTurn;
    private final Map<String, Integer> scoreboard;
    private final GamePhase gamePhase;
    private final Map<String, Boolean> playerDeadLock;

    /**
     * Constructs the update.
     * @param modelUpdater instance of the model updater referenced to execute updates
     * @param currentTurn match's current turn
     * @param scoreboard map of points scored by each player
     * @param gamePhase match's current game phase
     * @param playerDeadLock map of players who can't execute any more actions.
     */
    public SetBoardStateUpdate(ModelUpdater modelUpdater, int currentTurn, Map<String, Integer> scoreboard, GamePhase gamePhase, Map<String, Boolean> playerDeadLock) {
        super(modelUpdater);
        this.currentTurn = currentTurn;
        this.scoreboard = scoreboard;
        this.gamePhase = gamePhase;
        this.playerDeadLock = playerDeadLock;
    }

    /**
     * Updates the view defining the initial state of the board for subsequent calls.
     */
    @Override
    public void update() {
        modelUpdater.setBoardState(currentTurn, scoreboard, gamePhase, playerDeadLock);
    }
}
