package it.polimi.ingsw.network.rmi.update.player;

import it.polimi.ingsw.view.ModelUpdater;

/**
 * This class represents a rmi update.
 * <p>
 *     It notifies about a card removal.
 * </p>
 */
public class HandRemoveCard extends PlayerTypeUpdate{
    private final String removedCard;

    /**
     * Constructs the update.
     * @param modelUpdater instance of the model updater referenced to execute updates
     * @param nickname unique id of the player
     * @param removedCard id of the removed card
     */
    public HandRemoveCard(ModelUpdater modelUpdater, String nickname, String removedCard) {
        super(modelUpdater, nickname);
        this.removedCard = removedCard;
    }

    /**
     * Updates the view removing the specified card from the stated player's hand.
     */
    @Override
    public void update() {
        modelUpdater.playerHandRemoveCard(nickname, removedCard);
    }
}
