package it.polimi.ingsw.model.cards.cardstrategies;
import it.polimi.ingsw.model.PlayArea;
import it.polimi.ingsw.model.cards.GoldCard;

/**
 * This class implements the "gold strategy" representing the
 * card that gives a fixed amount of points on placement.
 */
public class SimpleGoldCard implements GoldCardStrategy {
    /**
     * Default constructor.
    */
    public SimpleGoldCard(){}

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

    /**
     * Implementation of the equals method. <br>
     * This object is only identified by its class.
     * @param other the object to compare this to.
     * @return true if the other object is also a SimpleGoldCard.
     */
    @Override
    public boolean equals(Object other) {
        if(other == this) return true;
        // A simple gold card is identified only by its class
        return other instanceof SimpleGoldCard;
    }
}
