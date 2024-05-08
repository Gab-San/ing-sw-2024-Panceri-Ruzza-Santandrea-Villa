package it.polimi.ingsw.model.cards.cardstrategies;
import it.polimi.ingsw.model.PlayArea;
import it.polimi.ingsw.model.cards.GoldCard;

public class SimpleGoldCard implements GoldCardStrategy {
    /**
     * Returns the score multiplier for this gold card (= 1).
     * @param playArea current player's play area
     * @param card the card that is being played
     * @return the score multiplier of this gold card
     */
    @Override
    public int calculateSolves(PlayArea playArea, GoldCard card) {
        return 1;
    }

    @Override
    public boolean equals(Object other) {
        if(other == this) return true;
        // A simple gold card is identified only by its class
        return other instanceof SimpleGoldCard;
    }
}
