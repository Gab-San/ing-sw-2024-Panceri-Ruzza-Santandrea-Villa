package it.polimi.ingsw.model.cards;
import it.polimi.ingsw.model.cards.cardpatterns.GoldCardStrategy;
import it.polimi.ingsw.model.enums.GameResource;

import java.util.Hashtable;

public class GoldCard extends PlayCard{
    Hashtable<GameResource, Integer> placementCost;
    GoldCardStrategy goldStrat;

    @Override
    Hashtable<GameResource, Integer> getPlacementCost() {
        return placementCost;
    }

    int calculatePointsOnPlace(PlayArea playArea){
        return pointsOnPlace*goldStrat.calculateSolves(playArea);
    }
}
