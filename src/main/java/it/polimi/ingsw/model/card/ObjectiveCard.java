package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.PlayArea;

public class ObjectiveCard extends Card {
    private final ObjectiveStrategy strategy;

    public ObjectiveCard(ObjectiveStrategy strategy, int pointsPerSolve){
        this.strategy = strategy;
    }

    public int calculatePoints(PlayArea p){
        return strategy.calculatePoints(p);
    }
}
