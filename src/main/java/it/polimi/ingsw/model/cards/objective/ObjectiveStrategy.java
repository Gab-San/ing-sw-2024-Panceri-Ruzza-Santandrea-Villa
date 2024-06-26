package it.polimi.ingsw.model.cards.objective;

import it.polimi.ingsw.model.PlayArea;
import org.jetbrains.annotations.NotNull;

/**
 * Common interface for all objectives. Uses strategy pattern.
 */
public interface ObjectiveStrategy {
    /**
     * Calculates the number of times the strategy is solved.
     * @param playArea the playArea on which to calculate #solves of this objective
     * @return number of times this objective is satisfied on the given playArea
     */
    int calculateSolves(@NotNull PlayArea playArea);

    /**
     * Returns a string representing the name of the strategy.
     * @return the name of this strategy
     */
    String toString();
}
