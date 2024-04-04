package it.polimi.ingsw.model.cards.objective;

import it.polimi.ingsw.model.DoubleMap;
import it.polimi.ingsw.model.DoubleMapRO;
import it.polimi.ingsw.model.PlayArea;
import it.polimi.ingsw.model.cards.PlaceableCard;
import it.polimi.ingsw.model.enums.GameResource;

public class PatternObjectiveStrategy implements ObjectiveStrategy{
    PatternObjective pattern;
    // implement Pattern as an Enum PatternObjective.PatternName(...)
    // then .getPattern() to use in calculatePoints (this way all patterns are hard coded)

    public PatternObjectiveStrategy(PatternObjective pattern){
        this.pattern = pattern;
    }

    @Override
    public int calculateSolves(PlayArea p) {
        DoubleMapRO<PlaceableCard> cardMatrix = p.getCardMatrix();
        DoubleMapRO<GameResource> pattern = this.pattern.getPattern();
        DoubleMap<Boolean> checked = new DoubleMap<>();
        int numOfSolves = 0;

        // how to determine starting spot ??
        // how to determine size of cardMatrix ??

        // recursive calls from starting card?
        // keep maxRow, maxCol and use loops?


        // *TO DO* algorithm to check for the pattern
        return numOfSolves;
    }
}
