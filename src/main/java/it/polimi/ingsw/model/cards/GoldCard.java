package it.polimi.ingsw.model.cards;
import it.polimi.ingsw.model.PlayArea;
import it.polimi.ingsw.model.Point;
import it.polimi.ingsw.model.cards.cardstrategies.GoldCardStrategy;
import it.polimi.ingsw.model.enums.GameResource;

import java.security.InvalidParameterException;
import java.util.*;

/**
 * This class represents the gold card, which is mainly responsible for scoring points.
 */
public class GoldCard extends PlayCard{
    Hashtable<GameResource, Integer> placementCost;
    GoldCardStrategy goldStrat;

    public GoldCard(GameResource backResource, int pointsOnPlace, Hashtable<GameResource, Integer> plCost,
                    GoldCardStrategy goldStrat, Corner... corners) throws InvalidParameterException {
        super(backResource, pointsOnPlace, corners);
        this.goldStrat = goldStrat;
        this.placementCost = new Hashtable<>();
        // Copying information from the constructed object to the card
        for(GameResource res: plCost.keySet()){
            this.placementCost.put(res, plCost.get(res));
        }
    }

    private GoldCard(Point placement, GoldCard oldCard){
        super(placement, oldCard);
        this.placementCost = oldCard.placementCost;
        this.goldStrat = oldCard.goldStrat;
    }
    public boolean equals(GoldCard other){
        return super.equals(other) &&
                goldStrat.equals(other.goldStrat);
    }
    @Override
    public boolean equals(Object other){
        if (other == this) return true;
        if(other instanceof GoldCard)
            return equals((GoldCard) other);
        else
            return false;
    }

    @Override
    public Map<GameResource, Integer> getPlacementCost() {
        return placementCost;
    }

    @Override
    public int calculatePointsOnPlace(PlayArea playArea){
        return pointsOnPlace * goldStrat.calculateSolves(playArea, this);
    }

    @Override
    public PlaceableCard setPosition(Point placement) {
        return new GoldCard(placement, this);
    }
}