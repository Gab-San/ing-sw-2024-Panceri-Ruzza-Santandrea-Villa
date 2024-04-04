package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.GameResource;
import it.polimi.ingsw.model.PlayArea;

import java.util.*;

public abstract class PlayCard extends PlaceableCard{
    protected GameResource backResource;
    protected int pointsOnPlace;

    @Override
    public Map<GameResource, Integer> getCardResources() {
        int[] resourcesCount = new int[7];
        Set<CornerDirection> cornerKeys = corners.keySet();

        if(!flipped){
            for(CornerDirection cornDir: cornerKeys){
                GameResource res = corners.get(cornDir).getResource();
                if(res != null){
                    resourcesCount[res.getResourceIndex()]++;
                }
            }
        } else {
            resourcesCount[backResource.getResourceIndex()]++;
        }

        HashMap<GameResource, Integer> countedResources = new HashMap<GameResource, Integer>();
        for (GameResource r : GameResource.values()){
            countedResources.put(r, resourcesCount[r.getResourceIndex()]);
        }

        return countedResources;
    }

    @Override
    public GameResource getCardColor() {
        return backResource;
    }

    /**
     * @return a table mapping the resources needed and the count of said resources.
     */
    public abstract Map<GameResource, Integer> getPlacementCost();

    /**
     * @param playArea associated with the player who is currently making the action
     * @return the points received from the placement
     */
    public abstract int calculatePointsOnPlace(PlayArea playArea);
}
