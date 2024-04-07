package it.polimi.ingsw.model.cards.cardstrategies;

import it.polimi.ingsw.model.PlayArea;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.GoldCard;
import it.polimi.ingsw.model.enums.CornerDirection;
import java.util.NoSuchElementException;

public class CornerCoverGoldCard implements GoldCardStrategy{
    @Override
    public int calculateSolves(PlayArea pA, GoldCard card) {
        int numCorners = 0;

        for (CornerDirection dir : CornerDirection.values()){
            boolean coversCorner = false;
            try{
                Corner corner = card.getCorner(dir);
                // corner occupied and visible == it is covering another corner
                coversCorner = corner.isOccupied() && corner.isVisible() ;
            }catch (NoSuchElementException e){ // corner is filled
                try {
                    coversCorner = pA.getCardMatrix().get(card.getPosition().move(dir)) != null;
                } catch(Exception nullPosition){
                    // TODO handle exception
                    nullPosition.printStackTrace();
                    System.err.println("Trying to access null position");
                }
            }
            numCorners += coversCorner ? 1 : 0;
        }

        return numCorners;
    }
}
