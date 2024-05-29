package it.polimi.ingsw.network.rmi.update;

import it.polimi.ingsw.view.ModelUpdater;

public class NotifyEndGameUpdate extends RMIUpdate {
    private final String nickname;
    private final int score;
    public NotifyEndGameUpdate(ModelUpdater modelUpdater, String nickname, int score) {
        super(modelUpdater);
        this.nickname = nickname;
        this.score = score;
    }
    public NotifyEndGameUpdate(ModelUpdater modelUpdater){
        this(modelUpdater, null, 0);
    }

    @Override
    public void update() {
        if(nickname == null){
            modelUpdater.notifyEndgame();
            return;
        }
        modelUpdater.notifyEndgame(nickname, score);
    }
}
