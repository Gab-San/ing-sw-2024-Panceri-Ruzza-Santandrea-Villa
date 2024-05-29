package it.polimi.ingsw.network.rmi.update.player;

import it.polimi.ingsw.view.ModelUpdater;

import java.util.List;

public class SetPlayerHandUpdate extends PlayerTypeUpdate{
    private final List<String> playCards;
    private final List<String> objectiveCards;
    private final String startingCard;
    public SetPlayerHandUpdate(ModelUpdater modelUpdater, String nickname, List<String> playCards, List<String> objectiveCards, String startingCard) {
        super(modelUpdater, nickname);
        this.playCards = playCards;
        this.objectiveCards = objectiveCards;
        this.startingCard = startingCard;
    }

    @Override
    public void update() {
        modelUpdater.setPlayerHandState(nickname, playCards, objectiveCards, startingCard);
    }
}
