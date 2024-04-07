package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.PlayArea;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.objective.ObjectiveStrategy;

public class ObjectiveCard extends Card {
    private final ObjectiveStrategy strategy;

    public ObjectiveCard(ObjectiveStrategy strategy, int pointsPerSolve){
        this.strategy = strategy;
    }

    public int calculatePoints(PlayArea p){
        return strategy.calculateSolves(p);
    }
}
