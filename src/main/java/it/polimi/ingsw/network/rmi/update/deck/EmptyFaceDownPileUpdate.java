package it.polimi.ingsw.network.rmi.update.deck;

import it.polimi.ingsw.view.ModelUpdater;

public class EmptyFaceDownPileUpdate extends DeckTypeUpdate{
    public EmptyFaceDownPileUpdate(ModelUpdater modelUpdater, char deck) {
        super(modelUpdater, deck);
    }

    @Override
    public void update() {
        modelUpdater.emptyFaceDownPile(deck);
    }
}
