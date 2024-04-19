package it.polimi.ingsw.model.cards.cardstrategies;

import it.polimi.ingsw.model.PlayArea;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.GoldCard;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.GameResource;

public class CornerCoverGoldCard implements GoldCardStrategy{
    /**
     * This method calculates how many corners the placed card covers.
     * <p>
     *     This number multiplied by the points on placement will result
     *     in the score for placing this gold card.
     * </p>
     * @param pA current player's play area
     * @param card the card that is being placed
     * @return the score multiplier of this gold card
     */
    @Override
    public int calculateSolves(PlayArea pA, GoldCard card) {
        int numCorners = 0;

        for (CornerDirection dir : CornerDirection.values()){
            boolean coversCorner;
            Corner corner = card.getCorner(dir);
            if(corner.getResource() != GameResource.FILLED)
                // corner not filled, occupied and visible == it is covering another corner
                coversCorner = corner.isOccupied() && corner.isVisible() ;
            else {
                try {
                    //TODO This try branch can be reached only if getting the reference to the placed card
                    coversCorner = pA.getCardMatrix().get(card.getPosition().move(dir)) != null;
                }catch (RuntimeException e){
                    coversCorner = pA.getCardMatrix().get(card.getCorner(dir).getCardRef().getPosition().move(dir)) != null;
                }
            }
            numCorners += coversCorner ? 1 : 0;
        }

        return numCorners;
    }

    @Override
    public boolean equals(Object other) {
        if(other == this) return true;
        // This strat is identified only by its class
        return other instanceof CornerCoverGoldCard;
    }
}
