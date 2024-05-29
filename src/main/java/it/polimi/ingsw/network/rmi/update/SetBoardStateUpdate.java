package it.polimi.ingsw.network.rmi.update;

import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.view.ModelUpdater;

import java.util.Map;

public class SetBoardStateUpdate extends RMIUpdate {
    private final int currentTurn;
    private final Map<String, Integer> scoreboard;
    private final GamePhase gamePhase;
    private final Map<String, Boolean> playerDeadLock;

    public SetBoardStateUpdate(ModelUpdater modelUpdater, int currentTurn, Map<String, Integer> scoreboard, GamePhase gamePhase, Map<String, Boolean> playerDeadLock) {
        super(modelUpdater);
        this.currentTurn = currentTurn;
        this.scoreboard = scoreboard;
        this.gamePhase = gamePhase;
        this.playerDeadLock = playerDeadLock;
    }

    @Override
    public void update() {
        modelUpdater.setBoardState(currentTurn, scoreboard, gamePhase, playerDeadLock);
    }
}
