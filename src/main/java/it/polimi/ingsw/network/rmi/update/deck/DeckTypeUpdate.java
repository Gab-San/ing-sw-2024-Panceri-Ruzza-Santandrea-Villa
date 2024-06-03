package it.polimi.ingsw.network.rmi.update.deck;

import it.polimi.ingsw.network.rmi.update.RMIUpdate;
import it.polimi.ingsw.view.ModelUpdater;

/**
 * This class represents a rmi update.
 * <p>
 *     It is a base class for all updates dealing with decks.
 * </p>
 */
abstract public class DeckTypeUpdate extends RMIUpdate {
    /**
     * Deck on which changes are occurring.
     */
    protected final char deck;

    /**
     * Constructs the update.
     * @param modelUpdater instance of the model updater referenced to execute updates
     * @param deck identifier of the deck
     */
    protected DeckTypeUpdate(ModelUpdater modelUpdater, char deck) {
        super(modelUpdater);
        this.deck = deck;
    }
}
