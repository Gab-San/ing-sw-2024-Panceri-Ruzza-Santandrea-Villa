package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.PlayArea;
import it.polimi.ingsw.model.enums.Resource;

import java.util.Map;

public class ResourceObjectiveStrategy implements ObjectiveStrategy{
    private final int pointsPerSolve;
    private final Map<Resource, Integer> resourceForCompletion;

    public ResourceObjectiveStrategy(int pointsPerSolve, Map<Resource, Integer> resourceForCompletion) {
        this.pointsPerSolve = pointsPerSolve;
        this.resourceForCompletion = resourceForCompletion;
    }
    public int getPointsPerSolve() {
        return pointsPerSolve;
    }

    public int calculatePoints(PlayArea p){
        int numOfSolves = divideMap(p.getVisibleResources(), resourceForCompletion);

        return pointsPerSolve * numOfSolves;
    }

    // returns the maximum multiplier M that satisfies b(resource)*M <= a(resource) for all resources in b
    private int divideMap(Map<Resource, Integer> a, Map<Resource, Integer> b){
        return b.keySet().stream()
                .mapToInt( resource -> a.get(resource)/b.get(resource) )
                .min().orElse(0);
    }

}
