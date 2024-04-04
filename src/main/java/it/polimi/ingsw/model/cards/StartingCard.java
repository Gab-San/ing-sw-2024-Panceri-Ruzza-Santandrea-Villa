package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enums.*;

import java.util.*;
import java.util.Set;

public class StartingCard extends PlaceableCard {
    private GameResource[] centralFrontResources;
    private Hashtable<CornerDirection, GameResource> frontCornersResources;

    @Override
    public Map<GameResource, Integer> getCardResources() {
        int[] resourcesCount = new int[7];

        if(!flipped) {
            Set<CornerDirection> cornerKeys = corners.keySet();

            for (CornerDirection cornDir : cornerKeys) {
                GameResource res = corners.get(cornDir).getResource();
                if (res != null) {
                    resourcesCount[res.getResourceIndex()]++;
                }
            }

        } else{

            Set<CornerDirection> cornerKeys = frontCornersResources.keySet();
            for(CornerDirection cornDir: cornerKeys){
                GameResource res = corners.get(cornDir).getResource();
                if(res != null){
                    resourcesCount[res.getResourceIndex()]++;
                }
            }

            for(GameResource res: centralFrontResources){
                resourcesCount[res.getResourceIndex()]++;
            }
        }

        Hashtable<GameResource, Integer> countedResources = new Hashtable<GameResource, Integer>();
        for (GameResource r : GameResource.values()){
            countedResources.put(r, resourcesCount[r.getResourceIndex()]);
        }

        return countedResources;
    }

    @Override
    public GameResource getCardColor() {
        return null;
    }
}
