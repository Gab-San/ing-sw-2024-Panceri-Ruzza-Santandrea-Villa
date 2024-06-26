package it.polimi.ingsw.model.cards.objective;

import it.polimi.ingsw.model.PlayArea;
import it.polimi.ingsw.GameResource;
import org.jetbrains.annotations.NotNull;

import java.util.Hashtable;
import java.util.Map;

/**
 * Objective strategy for objectives that require a specific amount of resources
 */
public class ResourceObjectiveStrategy implements ObjectiveStrategy{
    /**
     * A map of each resource-quantity pair
     * required to solve this objective.
     */
    private final Map<GameResource, Integer> resourceForCompletion;

    /**
     * Construct this strategy, copying the argument map by value.
     * @param resourceForCompletion a map of each resource-quantity pair
     *                             required to solve this objective.
     *
     */
    public ResourceObjectiveStrategy(Map<GameResource, Integer> resourceForCompletion) {
        this.resourceForCompletion = new Hashtable<>();
        this.resourceForCompletion.putAll(resourceForCompletion);
    }

    /**
     * Calculates how many times this objective has been solved on the given playArea.
     * @param playArea the playArea on which to calculate #solves of this objective
     * @return the number of times this objective was solved on the playArea.
     */
    public int calculateSolves(@NotNull PlayArea playArea){
        return divideMap(playArea.getVisibleResources(), resourceForCompletion);
    }

    /**
     * @param a dividend map
     * @param b divisor map
     * @return the maximum multiplier M that satisfies b.get(resource)*M <= a.get(resource) for all resources in b
    */
    private int divideMap(Map<GameResource, Integer> a, Map<GameResource, Integer> b){
        return b.keySet().stream()
                .mapToInt( resource -> a.get(resource)/b.get(resource) )
                .min().orElse(0);
    }

    @Override
    public boolean equals(Object other){
        if (other == this) return true;
        if(!(other instanceof ResourceObjectiveStrategy resObj)) return false;

        return resourceForCompletion.equals(resObj.resourceForCompletion);
    }
    @Override
    public String toString() {
        return "Resources Needed: " +resourceForCompletion.toString();
    }
}
