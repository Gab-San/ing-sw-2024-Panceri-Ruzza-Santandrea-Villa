package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GamePoint;

/**
 * This interfaced defines a corner listener.
 */
public interface CornerListener {
    /**
     * Positions the selected card on top of the clicked corner.
     * @param cardID identifier of the card on which the corner is placed
     * @param position corner's card position
     * @param direction corner direction relative to the center of the card
     * @throws IllegalStateException if no card is selected
     */
    void placeOnCorner(String cardID, GamePoint position, CornerDirection direction) throws IllegalStateException;
}
