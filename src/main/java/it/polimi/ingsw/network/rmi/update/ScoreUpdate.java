package it.polimi.ingsw.network.rmi.update;

import it.polimi.ingsw.network.rmi.update.player.PlayerTypeUpdate;
import it.polimi.ingsw.view.ModelUpdater;

public class ScoreUpdate extends PlayerTypeUpdate {
    private final int score;
    public ScoreUpdate(ModelUpdater modelUpdater, String nickname, int score) {
        super(modelUpdater, nickname);
        this.score = score;
    }

    @Override
    public void update() {
        modelUpdater.updateScore(nickname, score);
    }
}
