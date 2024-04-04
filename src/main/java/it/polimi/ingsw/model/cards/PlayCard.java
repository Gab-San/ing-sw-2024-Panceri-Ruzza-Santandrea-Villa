package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.GameResource;

import java.util.Hashtable;
import java.util.Set;

public abstract class PlayCard extends PlaceableCard{
    GameResource backResource;
    int pointsOnPlace;

    @Override
    public int[] getCardResources() {
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
        return resourcesCount;
    }

    @Override
    public GameResource getCardColor() {
        return backResource;
    }

    /**
     * @return a table mapping the resources needed and the count of said resources.
     */
    abstract Hashtable<GameResource, Integer> getPlacementCost();

    /**
     * @param playArea associated with the player who is currently making the action
     * @return the points received from the placement
     */
    abstract int calculatePointsOnPlace(PlayArea playArea);
}
