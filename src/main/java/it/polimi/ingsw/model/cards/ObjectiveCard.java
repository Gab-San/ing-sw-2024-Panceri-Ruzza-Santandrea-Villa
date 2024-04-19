package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.PlayArea;
import it.polimi.ingsw.model.cards.objective.ObjectiveStrategy;

//TODO add class description
/**
 * This represents a generic card.
 * <p>
 * This is the highest level abstraction of a card, representing the most simple form of it being a blank
 * rectangular that can be flipped.
 * </p>
 * <p>
 * By default when instantiated a card is facing downwards (displaying back)
 * </p>
 */
public class ObjectiveCard extends Card {
    private final ObjectiveStrategy strategy;
    private final int pointsPerSolve;

    /**
     * Constructor for objective card.
     * <p>
     *     It takes the strategy with which the points must be calculated
     *     and the points multiplier.
     * </p>
     * @param strategy objective
     * @param pointsPerSolve value of one solve
     */
    public ObjectiveCard(ObjectiveStrategy strategy, int pointsPerSolve){
        this.strategy = strategy;
        this.pointsPerSolve = pointsPerSolve;
    }

    /**
     * Returns the points scored at the end of the round.
     * <p>
     *     If the play area of the player suffices the
     *     strategy requirements, points are scored.
     * </p>
     * @param p player play area
     * @return the points scored relative to the objective
     */
    public int calculatePoints(PlayArea p){
        return pointsPerSolve * strategy.calculateSolves(p);
    }
    //TODO implement equals
}
