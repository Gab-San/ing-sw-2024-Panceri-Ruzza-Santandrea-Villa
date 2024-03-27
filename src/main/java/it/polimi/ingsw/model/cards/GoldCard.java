package it.polimi.ingsw.model.cards;
import it.polimi.ingsw.model.cards.cardpatterns.GoldCardStrategy;
import it.polimi.ingsw.model.enums.GameResource;

public class GoldCard extends PlayCard{
    Map<GameResource, Integer> placementCost;
    GoldCardStrategy goldStrat;
    int calculatePointsOnPlace(PlayArea pA){
        return pointsOnPlace*goldStrat.calculateSolves(pA);
    }
}
