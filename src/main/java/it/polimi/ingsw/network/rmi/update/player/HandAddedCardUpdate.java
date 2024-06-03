package it.polimi.ingsw.network.rmi.update.player;

import it.polimi.ingsw.view.ModelUpdater;

/**
 * This class represents a rmi update.
 * <p>
 *     Notifies of a playable card addition to the player's hand.
 * </p>
 */
public class HandAddedCardUpdate extends PlayerTypeUpdate{
    private final String drawnCardId;
    /**
     * Constructs the update.
     * @param modelUpdater instance of the model updater referenced to execute updates
     * @param nickname unique id of the player
     * @param drawnCardId identifier of the drawn card
     */
    public HandAddedCardUpdate(ModelUpdater modelUpdater, String nickname, String drawnCardId) {
        super(modelUpdater, nickname);
        this.drawnCardId = drawnCardId;
    }

    /**
     * Updates the view adding the drawn card to the players' hand.
     */
    @Override
    public void update() {
        modelUpdater.playerHandAddedCardUpdate(nickname, drawnCardId);
    }
}
