package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.DoubleMapRO;
import it.polimi.ingsw.model.PlayArea;
import it.polimi.ingsw.model.enums.Resource;

public class PatternObjectiveStrategy implements ObjectiveStrategy{
    private int pointsPerSolve;
    PatternObjective pattern;
    // implement Pattern as an Enum PatternObjective.PatternName(...)
    // then .getPattern() to use in calculatePoints (this way all patterns are hard coded)

    public PatternObjectiveStrategy(int pointsPerSolve, PatternObjective pattern){
        this.pointsPerSolve = pointsPerSolve;
        this.pattern = pattern;
    }

    @Override
    public int calculatePoints(PlayArea p) {
        DoubleMapRO<Integer, Integer, PlaceableCard> cardMatrix = p.getCardMatrix();
        DoubleMapRO<Integer, Integer, Resource> pattern = this.pattern.getPattern();

        // how to determine starting spot ??
        // how to determine size of cardMatrix ??

        // *TO DO* algorithm to check for the pattern
        return 0; // temp
    }
}
