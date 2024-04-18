package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.PlayArea;
import it.polimi.ingsw.model.cards.objective.ObjectiveStrategy;

public class ObjectiveCard extends Card {
    private final ObjectiveStrategy strategy;
    private final int pointsPerSolve;

    public ObjectiveCard(ObjectiveStrategy strategy, int pointsPerSolve){
        this.strategy = strategy;
        this.pointsPerSolve = pointsPerSolve;
    }

    public int calculatePoints(PlayArea p){
        return pointsPerSolve * strategy.calculateSolves(p);
    }
}
