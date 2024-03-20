package it.polimi.ingsw.model.cards.objective;

import it.polimi.ingsw.model.PlayArea;
import it.polimi.ingsw.model.cards.Card;

public class ObjectiveCard extends Card {
    private final ObjectiveStrategy strategy;

    public ObjectiveCard(ObjectiveStrategy strategy, int pointsPerSolve){
        this.strategy = strategy;
    }

    public int calculatePoints(PlayArea p){
        return strategy.calculateSolves(p);
    }
}
