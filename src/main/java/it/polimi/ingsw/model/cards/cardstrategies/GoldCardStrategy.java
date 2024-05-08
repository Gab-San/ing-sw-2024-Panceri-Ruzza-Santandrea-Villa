package it.polimi.ingsw.model.cards.cardstrategies;
import it.polimi.ingsw.model.PlayArea;
import it.polimi.ingsw.model.cards.GoldCard;

/**
 * Common interface for all gold card strategies to calculate the score multiplier on card's placement
 */
public interface GoldCardStrategy {
    /**
     * This method calculates the score multiplier for the card placed on playArea.
     * <p>
     *     Each strategy has a different calculation for the score multiplier.
     * </p>
     * @param playArea current player's play area
     * @param card the card that is being placed
     * @return the score multiplier of this gold card
     */
    int calculateSolves(PlayArea playArea, GoldCard card);
}
