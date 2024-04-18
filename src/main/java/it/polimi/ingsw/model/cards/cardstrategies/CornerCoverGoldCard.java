package it.polimi.ingsw.model.cards.cardstrategies;

import it.polimi.ingsw.model.PlayArea;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.GoldCard;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.GameResource;

public class CornerCoverGoldCard implements GoldCardStrategy{
    @Override
    public int calculateSolves(PlayArea pA, GoldCard card) {
        int numCorners = 0;

        for (CornerDirection dir : CornerDirection.values()){
            boolean coversCorner = false;
            Corner corner = card.getCorner(dir);
            if(corner.getResource() != GameResource.FILLED)
                // corner not filled, occupied and visible == it is covering another corner
                coversCorner = corner.isOccupied() && corner.isVisible() ;
            else {
                try {
                    coversCorner = pA.getCardMatrix().get(card.getPosition().move(dir)) != null;
                }catch (RuntimeException e){
                    // FIXME: handle error if position == null??
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
