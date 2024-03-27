package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enums.*;

import java.util.Set;

public class StartingCard extends PlaceableCard {
    GameResource[] centralResources;
    //TODO: the resources on the corners when facing the front of the starting cards are not considered
    @Override
    public int[] getCardResources() {
        int[] resources = new int[7];
        Set<CornerDirection> cornKey = corners.keySet();

        //In any case I can add the resources
        for (CornerDirection cornDir : cornKey) {
            GameResource res = corners.get(cornDir).getResource();
            if(res != null){
                resources[res.getResourceIndex()]++;
            }
        }

        //If the central resources are displayed then they are considered
        if(flipped){
            for(GameResource res: centralResources){
                resources[res.getResourceIndex()]++;
            }
        }

        return resources;
    }

    @Override
    public GameResource getCardColor() {
        return null;
    }
}
