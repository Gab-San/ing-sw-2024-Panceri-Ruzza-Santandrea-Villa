package it.polimi.ingsw.network.rmi.update.deck;

import it.polimi.ingsw.view.ModelUpdater;

public class OnlyRevealedSetDeckStateUpdate extends DeckTypeUpdate {
    private final String revealedId;
    private final int cardPosition;
    public OnlyRevealedSetDeckStateUpdate(ModelUpdater modelUpdater, char deck, String revealedId, int cardPosition) {
        super(modelUpdater, deck);
        this.revealedId = revealedId;
        this.cardPosition = cardPosition;
    }


    @Override
    public void update() {
        modelUpdater.setDeckState(deck,revealedId,cardPosition);
    }
}
