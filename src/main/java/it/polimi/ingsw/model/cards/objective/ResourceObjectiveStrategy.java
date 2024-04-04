package it.polimi.ingsw.model.cards.objective;

import it.polimi.ingsw.model.PlayArea;
import it.polimi.ingsw.model.enums.GameResource;

import java.util.Map;

public class ResourceObjectiveStrategy implements ObjectiveStrategy{
    private final Map<GameResource, Integer> resourceForCompletion;

    public ResourceObjectiveStrategy(int pointsPerSolve, Map<GameResource, Integer> resourceForCompletion) {
        this.resourceForCompletion = resourceForCompletion;
    }

    public int calculateSolves(PlayArea p){
        return divideMap(p.getVisibleResources(), resourceForCompletion);
    }

    // returns the maximum multiplier M that satisfies b(resource)*M <= a(resource) for all resources in b
    private int divideMap(Map<GameResource, Integer> a, Map<GameResource, Integer> b){
        return b.keySet().stream()
                .mapToInt( resource -> a.get(resource)/b.get(resource) )
                .min().orElse(0);
    }

}
