package it.polimi.ingsw.network.rmi.update;

import it.polimi.ingsw.network.rmi.update.player.PlayerTypeUpdate;
import it.polimi.ingsw.view.ModelUpdater;

/**
 * This class represents a rmi update.
 * <p>
 *  It notifies about a change in one of the scores
 * </p>
 */
public class ScoreUpdate extends PlayerTypeUpdate {
    private final int score;

    /**
     * Constucts the update.
     * @param modelUpdater instance of the model updater referenced to execute updates
     * @param nickname unique id of the player who scored points
     * @param score value of the current score
     */
    public ScoreUpdate(ModelUpdater modelUpdater, String nickname, int score) {
        super(modelUpdater, nickname);
        this.score = score;
    }

    /**
     * Updates current score of the specified player in view.
     */
    @Override
    public void update() {
        modelUpdater.updateScore(nickname, score);
    }
}
