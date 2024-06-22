package it.polimi.ingsw.view.gui;

/**
 * This interface defines the skeleton of a deck listener.
 */
public interface DeckListener {
    /**
     * Extrapolates the selected position in the deck by matching
     * the card the player wants to draw.
     * @param deckId deck identifier
     * @param cardPosition card position in the deck
     */
    void setSelectedPosition(char deckId, int cardPosition);
}
