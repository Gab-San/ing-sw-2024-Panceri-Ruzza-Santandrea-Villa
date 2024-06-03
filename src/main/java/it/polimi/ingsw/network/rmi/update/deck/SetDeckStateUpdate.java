package it.polimi.ingsw.network.rmi.update.deck;

import it.polimi.ingsw.view.ModelUpdater;

/**
 * This class represents a rmi update.
 * <p>
 *     Sets the current deck state.
 * </p>
 */
public class SetDeckStateUpdate extends DeckTypeUpdate{
    private final String topId;
    private final String firstId;
    private final String secondId;

    /**
     * Constructs the update, when each position in the deck has a card.
     * <p>
     *    If the deck has at least three card, all three positions in the deck are covered.
     * </p>
     * @param modelUpdater instance of the model updater referenced to execute updates
     * @param deck identifier of the deck
     * @param topId identifier of the deck's top card
     * @param firstId identifier of the first revealed card
     * @param secondId identifier of the second revealed card
     */
    public SetDeckStateUpdate(ModelUpdater modelUpdater, char deck, String topId, String firstId, String secondId) {
        super(modelUpdater, deck);
        this.topId = topId;
        this.firstId = firstId;
        this.secondId = secondId;
    }

    /**
     * Constructs the update, when the face-down pile is empty.
     * @param modelUpdater instance of the model updater referenced to execute updates
     * @param deck identifier of the deck
     * @param firstId identifier of the first revealed card
     * @param secondId identifier of the second revealed card
     */
    public SetDeckStateUpdate(ModelUpdater modelUpdater, char deck, String firstId, String secondId){
        this(modelUpdater, deck, null, firstId, secondId);
    }

    /**
     * Constructs the update.
     * @param modelUpdater instance of the model updater referenced to execute updates
     * @param deck identifier of the deck
     */
    public SetDeckStateUpdate(ModelUpdater modelUpdater, char deck){
        this(modelUpdater, deck, null, null, null);
    }

    /**
     * Updates initializing the deck state.
     */
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
