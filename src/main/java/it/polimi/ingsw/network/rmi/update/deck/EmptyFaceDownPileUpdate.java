package it.polimi.ingsw.network.rmi.update.deck;

import it.polimi.ingsw.view.ModelUpdater;

/**
 * This class represents a rmi update.
 * <p>
 *     Notifies that the face-down pile is empty.
 * </p>
 */
public class EmptyFaceDownPileUpdate extends DeckTypeUpdate{
    /**
     * Constructs the update.
     * @param modelUpdater instance of the model updater referenced to execute updates
     * @param deck identifier of the deck
     */
    public EmptyFaceDownPileUpdate(ModelUpdater modelUpdater, char deck) {
        super(modelUpdater, deck);
    }

    /**
     * Updates the face-down pile of the stated deck to empty.
     */
    @Override
    public void update() {
        modelUpdater.emptyFaceDownPile(deck);
    }
}
