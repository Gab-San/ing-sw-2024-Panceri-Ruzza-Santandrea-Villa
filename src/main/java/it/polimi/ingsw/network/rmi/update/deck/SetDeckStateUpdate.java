package it.polimi.ingsw.network.rmi.update.deck;

import it.polimi.ingsw.view.ModelUpdater;

public class SetDeckStateUpdate extends DeckTypeUpdate{
    private final String topId;
    private final String firstId;
    private final String secondId;
    public SetDeckStateUpdate(ModelUpdater modelUpdater, char deck, String topId, String firstId, String secondId) {
        super(modelUpdater, deck);
        this.topId = topId;
        this.firstId = firstId;
        this.secondId = secondId;
    }

    public SetDeckStateUpdate(ModelUpdater modelUpdater, char deck, String firstId, String secondId){
        this(modelUpdater, deck, null, firstId, secondId);
    }

    public SetDeckStateUpdate(ModelUpdater modelUpdater, char deck){
        this(modelUpdater, deck, null, null, null);
    }

    @Override
    public void update() {
        if(topId == null && firstId == null && secondId == null){
            modelUpdater.setEmptyDeckState(deck);
            return;
        }
        if(topId == null){
            modelUpdater.setDeckState(deck, firstId, secondId);
            return;
        }
        modelUpdater.setDeckState(deck, topId, firstId, secondId);
    }
}
