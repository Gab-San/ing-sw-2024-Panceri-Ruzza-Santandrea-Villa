package it.polimi.ingsw.network.rmi.update.deck;

import it.polimi.ingsw.view.ModelUpdater;

public class EmptyRevealUpdate extends DeckTypeUpdate{
    private final int cardPosition;
    public EmptyRevealUpdate(ModelUpdater modelUpdater, char deck, int cardPosition) {
        super(modelUpdater, deck);
        this.cardPosition = cardPosition;
    }

    @Override
    public void update() {
        modelUpdater.emptyReveal(deck, cardPosition);
    }
}
