package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.model.cards.ViewPlaceableCard;

/**
 * This interface defines an object which is listening to card's event.
 */
public interface CardListener {
    /**
     * Selects the current card and deselects all the other pressed cards.
     * @param card selected card
     */
    void setSelectedCard(ViewPlaceableCard card);
}
