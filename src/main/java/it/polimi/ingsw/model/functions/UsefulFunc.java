package it.polimi.ingsw.model.functions;

import it.polimi.ingsw.model.enums.GameResource;

import java.util.Hashtable;
import java.util.Map;

public class UsefulFunc {
    /**
     * This function converts a counting array of resources into a map.
     * All the information is preserved.
     * @param resourcesCount the counting array of resources
     * @return a map which keys are the resources and the values are the count of each.
     */
    public static Map<GameResource, Integer> resourceArrayToMap(int[] resourcesCount){
        Hashtable<GameResource, Integer> countedResources = new Hashtable<>();
        for (GameResource r : GameResource.values()){
            if(r != GameResource.FILLED)
                countedResources.put(r, resourcesCount[r.getResourceIndex()]);
        }

        return countedResources;
    }
}
