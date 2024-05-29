package it.polimi.ingsw.network.rmi.update.deck;

import it.polimi.ingsw.view.ModelUpdater;

public class DeckUpdate extends DeckTypeUpdate{
    private final String revealedId;
    private final int cardPosition;
    public DeckUpdate(ModelUpdater modelUpdater, char deck, String revealedId, int cardPosition) {
        super(modelUpdater, deck);
        this.revealedId = revealedId;
        this.cardPosition = cardPosition;
    }

    @Override
    public void update() {
        modelUpdater.deckUpdate(deck, revealedId, cardPosition);
    }
}
