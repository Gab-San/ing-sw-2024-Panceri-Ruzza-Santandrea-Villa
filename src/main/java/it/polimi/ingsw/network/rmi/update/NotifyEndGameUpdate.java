package it.polimi.ingsw.network.rmi.update;

import it.polimi.ingsw.view.ModelUpdater;

/**
 * This class represents a rmi update.
 *
 * <p>
 *     This update has two different compositions: it can either be triggered by
 *     a user reaching or exceeding score limit or by deck being empty.
 * </p>
 */
public class NotifyEndGameUpdate extends RMIUpdate {
    private final String nickname;
    private final int score;

    /**
     * Constructs the endgame update due to score.
     *
     * @param modelUpdater instance of the model updater referenced to execute updates
     * @param nickname unique id of the user who has reached or exceeded score limit first
     * @param score score with which they exceeded score limit
     */
    public NotifyEndGameUpdate(ModelUpdater modelUpdater, String nickname, int score) {
        super(modelUpdater);
        this.nickname = nickname;
        this.score = score;
    }

    /**
     * Constructs the endgame update due to emptied deck.
     * @param modelUpdater instance of the model updater referenced to execute updates
     */
    public NotifyEndGameUpdate(ModelUpdater modelUpdater){
        this(modelUpdater, null, 0);
    }

    /**
     * Updates the view notifying that endgame has been reached, including the cause.
     */
    @Override
    public void update() {
        if(nickname == null){
            modelUpdater.notifyEndgame();
            return;
        }
        modelUpdater.notifyEndgame(nickname, score);
    }
}
