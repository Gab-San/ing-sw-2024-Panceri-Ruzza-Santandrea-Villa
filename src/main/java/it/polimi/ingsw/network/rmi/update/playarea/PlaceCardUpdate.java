package it.polimi.ingsw.network.rmi.update.playarea;

import it.polimi.ingsw.network.rmi.update.player.PlayerTypeUpdate;
import it.polimi.ingsw.view.ModelUpdater;

/**
 * This class represents a rmi update.
 * <p>
 *     Notifies about a placed card on player's play area.
 * </p>
 */
public class PlaceCardUpdate extends PlayerTypeUpdate {
    private final String placeCardId;
    private final boolean placeOnFront;
    private final int row;
    private final int col;

    /**
     * Constructs the update.
     * @param modelUpdater instance of the model updater referenced to execute updates
     * @param nickname unique id of the player
     * @param placeCardId identifier of the placed card
     * @param row x-axis position of the placed card
     * @param col y-axis position of teh placed card
     * @param placeOnFront true if the card face is up, false otherwise
     */
    public PlaceCardUpdate(ModelUpdater modelUpdater, String nickname, String placeCardId, int row, int col, boolean placeOnFront) {
        super(modelUpdater, nickname);
        this.placeCardId = placeCardId;
        this.placeOnFront = placeOnFront;
        this.row = row;
        this.col = col;
    }

    /**
     * Updates the view placing a card on the specified player's play area.
     */
    @Override
    public void update() {
        modelUpdater.updatePlaceCard(nickname, placeCardId, row, col, placeOnFront);
    }
}
