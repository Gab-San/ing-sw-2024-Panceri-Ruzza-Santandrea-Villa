package it.polimi.ingsw.model.cards.cardstrategies;
import it.polimi.ingsw.model.PlayArea;
import it.polimi.ingsw.model.cards.GoldCard;

public interface GoldCardStrategy {
    /**
     * This method calculates how many corners the placed card covers.
     * <p>
     *     Each strategy has a different calculation for the score multiplier.
     * </p>
     * @param pA current player's play area
     * @param card the card that is being placed
     * @return the score multiplier of this gold card
     */
    public int calculateSolves(PlayArea pA, GoldCard card);
}
