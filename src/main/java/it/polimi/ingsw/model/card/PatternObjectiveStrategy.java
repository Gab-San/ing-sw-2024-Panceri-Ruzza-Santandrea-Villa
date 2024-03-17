package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.PlayArea;

public class PatternObjectiveStrategy implements ObjectiveStrategy{
    private int pointsPerSolve;
    // implement Pattern as an Enum PatternObjective.PatternName(...)
    // then .getPattern() to use in calculatePoints (this way all patterns are hard coded)

    public PatternObjectiveStrategy(int pointsPerSolve){
        this.pointsPerSolve = pointsPerSolve;
    }

    @Override
    public int calculatePoints(PlayArea p) {
        // *TO DO* algorithm to check for the pattern
        return 0; // temp
    }
}
