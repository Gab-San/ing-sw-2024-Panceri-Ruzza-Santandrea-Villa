package it.polimi.ingsw.network.rmi.update.deck;

import it.polimi.ingsw.view.ModelUpdater;

/**
 * This class represents a rmi update.
 * <p>
 *     Sets the state of the deck when the deck is composed of only one revealed card.
 * </p>
 */
public class OnlyRevealedSetDeckStateUpdate extends DeckTypeUpdate {
    private final String revealedId;
    private final int cardPosition;

    /**
     * Constructs the update.
     * @param modelUpdater instance of the model updater referenced to execute updates
     * @param deck identifier of the deck
     * @param revealedId
     * @param cardPosition
     */
    public OnlyRevealedSetDeckStateUpdate(ModelUpdater modelUpdater, char deck, String revealedId, int cardPosition) {
        super(modelUpdater, deck);
        this.revealedId = revealedId;
        this.cardPosition = cardPosition;
    }

    /**
     * Updates the view initializing the deck state.
     */
    @Override
    public void update() {
        modelUpdater.setDeckState(deck,revealedId,cardPosition);
    }
}
