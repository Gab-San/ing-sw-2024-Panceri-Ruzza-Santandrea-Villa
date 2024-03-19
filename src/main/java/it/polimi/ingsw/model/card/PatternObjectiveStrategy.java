package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.DoubleMap;
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
        DoubleMapRO<PlaceableCard> cardMatrix = p.getCardMatrix();
        DoubleMapRO<Resource> pattern = this.pattern.getPattern();
        DoubleMap<Boolean> checked = new DoubleMap<>();

        // how to determine starting spot ??
        // how to determine size of cardMatrix ??

        // recursive calls from starting card?
        // keep maxRow, maxCol and use loops?


        // *TO DO* algorithm to check for the pattern
        return 0; // temp
    }
}
