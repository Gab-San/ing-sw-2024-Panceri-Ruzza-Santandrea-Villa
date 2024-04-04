package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enums.*;

import java.util.Hashtable;
import java.util.Set;

public class StartingCard extends PlaceableCard {
    GameResource[] centralFrontResources;
    Hashtable<CornerDirection, GameResource> frontCornersResources;

    @Override
    public int[] getCardResources() {
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

        return resourcesCount;
    }

    @Override
    public GameResource getCardColor() {
        return null;
    }
}
