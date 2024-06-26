package it.polimi.ingsw.model.cards.cardstrategies;

import it.polimi.ingsw.model.PlayArea;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.GoldCard;
import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GameResource;

/**
 * This class represents the gold card strategy that gives point based on
 * how many corners the card covers.
 */
public class CornerCoverGoldCard implements GoldCardStrategy{
    /**
     * Default constructor.
     */
    public CornerCoverGoldCard(){}
    /**
     * This method calculates how many corners the placed card covers.
     * <p>
     *     This number multiplied by the points on placement will result
     *     in the score for placing this gold card.
     * </p>
     * @param playArea current player's play area
     * @param card the card that is being placed
     * @return the score multiplier of this gold card
     */
    @Override
    public int calculateSolves(PlayArea playArea, GoldCard card) {
        int numCorners = 0;

        for (CornerDirection dir : CornerDirection.values()){
            boolean coversCorner;
            Corner corner = card.getCorner(dir);
            if(corner.getResource() != GameResource.FILLED)
                // corner not filled, occupied and visible == it is covering another corner
                coversCorner = corner.isOccupied() && corner.isVisible() ;
            else {
                try {
                    coversCorner = playArea.getCardMatrix().get(card.getPosition().move(dir)) != null;
                }catch (IllegalStateException e){
                    coversCorner = playArea.getCardMatrix().get(card.getCorner(dir).getCardRef().getPosition().move(dir)) != null;
                }
            }
            numCorners += coversCorner ? 1 : 0;
        }

        return numCorners;
    }

    @Override
    public boolean equals(Object other) {
        if(other == this) return true;
        // This strategy is identified only by its class
        return other instanceof CornerCoverGoldCard;
    }
}
