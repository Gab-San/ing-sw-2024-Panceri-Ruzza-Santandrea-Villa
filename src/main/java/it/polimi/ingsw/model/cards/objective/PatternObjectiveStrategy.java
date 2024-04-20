package it.polimi.ingsw.model.cards.objective;

import it.polimi.ingsw.model.DoubleMap;
import it.polimi.ingsw.model.DoubleMapRO;
import it.polimi.ingsw.model.PlayArea;
import it.polimi.ingsw.model.Point;
import it.polimi.ingsw.model.cards.PlaceableCard;
import it.polimi.ingsw.model.enums.GameResource;

import java.util.*;

public class PatternObjectiveStrategy implements ObjectiveStrategy{
    private final PatternObjective pattern;
    // implement Pattern as an Enum PatternObjective.PatternName(...)
    // then .getPattern() to use in calculatePoints (this way all patterns are hard coded)

    public PatternObjectiveStrategy(PatternObjective pattern){
        this.pattern = pattern;
    }

    @Override
    public boolean equals(Object other){
        if (other == this) return true;
        if(!(other instanceof PatternObjectiveStrategy resObj)) return false;

        return pattern.equals(resObj.pattern);
    }
    // TODO: test algorithm to check for the Objective pattern
    /**
     * @author Ruzza
     * @param playArea the playArea on which to calculate #occurrences of this pattern
     * @return num of times this pattern occurs in playArea
     */
    @Override
    public int calculateSolves(PlayArea playArea) {
        Map<Point, PlaceableCard> cardMatrix = playArea.getCardMatrix();
        Map<Point, GameResource> pattern = this.pattern.getPattern();
        Set<Point> usedPoints = new HashSet<>();
        int numOfSolves = 0;

        // order is irrelevant, all points with cards will be checked eventually
        for (Point pos : cardMatrix.keySet()){
            boolean patternFound = true;
            // only look at cards in points necessary for the pattern (relative to center of this check)
            // terminate early with failure if that point:
            //     - has no card
            //     - was already used for this objective
            //     - has a card of the wrong color
            for(Point patternPos : pattern.keySet()){
                PlaceableCard card = cardMatrix.get(pos.add(patternPos));
                boolean invalidCard = card == null || card.getCardColour()==null || !card.getCardColour().equals(pattern.get(patternPos));
                boolean cardAlreadyUsed = usedPoints.contains(pos.add(patternPos));
                if(invalidCard || cardAlreadyUsed){
                    patternFound =  false;
                    break;
                }
            }
            if (patternFound){
                numOfSolves++;
                for(Point patternPos : pattern.keySet()){
                    usedPoints.add(pos.add(patternPos));
                }
            }
        }

        return numOfSolves;
    }
}
