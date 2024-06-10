package it.polimi.ingsw;

import java.io.Serializable;
import java.util.Map;

/**
 * Serializable record that represents the key information of a placed card in a playArea
 * @param row y coordinate of the card
 * @param col x coordinate of the card
 * @param cardId ID of the card
 * @param isFaceUp true if the card was placed front-side up
 * @param isCornerVisible map associating the Corner.isVisible() value of each corner of the card
 */
public record CardPosition(int row, int col, String cardId, boolean isFaceUp, Map<CornerDirection, Boolean> isCornerVisible) implements Serializable {

}
