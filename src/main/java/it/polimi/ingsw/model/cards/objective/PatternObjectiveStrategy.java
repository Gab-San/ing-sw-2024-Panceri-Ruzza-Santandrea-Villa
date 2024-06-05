package it.polimi.ingsw.model.cards.objective;
import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.PlaceableCard;
import it.polimi.ingsw.GameResource;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Objective strategy for objectives that use a 3x3 pattern of cards (identified by color)
 */
public class PatternObjectiveStrategy implements ObjectiveStrategy{
    private final PatternObjective pattern;

    public PatternObjectiveStrategy(PatternObjective pattern){
        this.pattern = pattern;
    }

    @Override
    public boolean equals(Object other){
        if (other == this) return true;
        if(!(other instanceof PatternObjectiveStrategy resObj)) return false;

        return pattern.equals(resObj.pattern);
    }

    @Override
    public int calculateSolves(@NotNull PlayArea playArea) {
        Map<GamePoint, PlaceableCard> cardMatrix = playArea.getCardMatrix();
        Map<GamePoint, GameResource> pattern = this.pattern.getPattern();
        Set<GamePoint> usedPoints = new HashSet<>();
        int numOfSolves = 0;

        List<GamePoint> cardPositions = cardMatrix.keySet().stream()
                .sorted(GamePoint::compare).toList();
        for (GamePoint pos : cardPositions){
            boolean patternFound = true;
            // only look at cards in points necessary for the pattern (relative to center of this check)
            // terminate early with failure if that point:
            //     - has no card
            //     - was already used for this objective
            //     - has a card of the wrong color
            for(GamePoint patternPos : pattern.keySet()){
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
                for(GamePoint patternPos : pattern.keySet()){
                    usedPoints.add(pos.add(patternPos));
                }
            }
        }
        return numOfSolves;
    }

    @Override
    public String toString() {
        return "PatternType: " +  pattern.toString();
    }
}
