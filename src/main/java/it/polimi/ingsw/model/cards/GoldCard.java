package it.polimi.ingsw.model.cards;
import it.polimi.ingsw.model.PlayArea;
import it.polimi.ingsw.model.cards.cardstrategies.GoldCardStrategy;
import it.polimi.ingsw.model.enums.GameResource;

import java.util.*;

public class GoldCard extends PlayCard{
    Hashtable<GameResource, Integer> placementCost;
    GoldCardStrategy goldStrat;

    @Override
    public Map<GameResource, Integer> getPlacementCost() {
        return placementCost;
    }

    @Override
    public int calculatePointsOnPlace(PlayArea playArea){
        return pointsOnPlace*goldStrat.calculateSolves(playArea, this);
    }
}