package it.polimi.ingsw.network.rmi.update.deck;

import it.polimi.ingsw.view.ModelUpdater;

/**
 * This class represents a rmi update.
 * <p>
 *     Notifies that the stated reveal position in the deck is empty.
 * </p>
 */
public class EmptyRevealUpdate extends DeckTypeUpdate{
    private final int cardPosition;
    /**
     * Constructs the update.
     * @param modelUpdater instance of the model updater referenced to execute updates
     * @param deck identifier of the deck
     * @param cardPosition empty card position
     */
    public EmptyRevealUpdate(ModelUpdater modelUpdater, char deck, int cardPosition) {
        super(modelUpdater, deck);
        this.cardPosition = cardPosition;
    }

    /**
     * Updates the view to empty the specified reveal position.
     */
    @Override
    public void update() {
        modelUpdater.emptyReveal(deck, cardPosition);
    }
}
