package it.polimi.ingsw.network.rmi.update.player;

import it.polimi.ingsw.view.ModelUpdater;

/**
 * This class represents a rmi update.
 * <p>
 *     Notifies about starting card being added to the specified player's hand
 * </p>
 */
public class HandSetStartingCard extends PlayerTypeUpdate{
    private final String startingCardId;

    /**
     * Constructs the update.
     * @param modelUpdater instance of the model updater referenced to execute updates
     * @param nickname unique id of the player
     * @param startingCardId the starting card given to the player
     */
    public HandSetStartingCard(ModelUpdater modelUpdater, String nickname, String startingCardId) {
        super(modelUpdater, nickname);
        this.startingCardId = startingCardId;
    }

    /**
     * Updates the view setting the specified player's starting card.
     */
    @Override
    public void update() {
        modelUpdater.playerHandSetStartingCard(nickname, startingCardId);
    }
}
