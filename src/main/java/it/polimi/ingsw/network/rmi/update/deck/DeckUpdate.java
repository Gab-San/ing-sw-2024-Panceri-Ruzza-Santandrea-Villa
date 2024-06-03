package it.polimi.ingsw.network.rmi.update.deck;

import it.polimi.ingsw.view.ModelUpdater;

/**
 * This class represents a rmi update.
 * <p>
 *     Notifies about a deck change.
 * </p>
 */
public class DeckUpdate extends DeckTypeUpdate{
    private final String revealedId;
    private final int cardPosition;

    /**
     * Constructs the update.
     * @param modelUpdater instance of the model updater referenced to execute updates
     * @param deck identifier of the deck
     * @param revealedId the card that was revealed when the change occurred
     * @param cardPosition the position in which the card was revealed
     */
    public DeckUpdate(ModelUpdater modelUpdater, char deck, String revealedId, int cardPosition) {
        super(modelUpdater, deck);
        this.revealedId = revealedId;
        this.cardPosition = cardPosition;
    }

    /**
     * Updates the specified deck composition.
     */
    @Override
    public void update() {
        modelUpdater.deckUpdate(deck, revealedId, cardPosition);
    }
}
