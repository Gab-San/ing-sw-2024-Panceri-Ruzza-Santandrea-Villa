package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Point;
import it.polimi.ingsw.model.enums.*;

import java.util.*;
import java.util.Set;

/**
 * This represents the starting card: the first card placed in the setup phase.
 */
public class StartingCard extends PlaceableCard {
    private final List<GameResource> centralFrontResources;
    private final Hashtable<CornerDirection, GameResource> frontCornersResources;

    /**
     * Constructs a blank card.
     */
    public StartingCard(){
        super();
        this.centralFrontResources = null;
        this.frontCornersResources = null;
    }

    /**
     * Constructs a starting card filled with the required information.
     * @param frontCornRes The possible front corners' resources
     * @param centralRes The resources shown in the center of the card
     * @param corners The corners of the card
     */
    //FIXME: how to set a filled frontCorner??
    public StartingCard(Hashtable<CornerDirection, GameResource> frontCornRes,
                        GameResource[] centralRes, Corner... corners){
        super(corners);

        this.centralFrontResources = new ArrayList<>();
        //TODO: Check if this copies;
        centralFrontResources.addAll(Arrays.asList(centralRes));

        this.frontCornersResources = new Hashtable<>();
        for(CornerDirection cornDir: frontCornRes.keySet()){
            this.frontCornersResources.put(cornDir, frontCornRes.get(cornDir));
        }
    }

    /**
     * Constructs a starting card from another one, adding the placement information.
     * @param placement The placement point of the card (starting cards are always placed at (0,0))
     * @param oldCard The unplaced card which is copied
     */
     private StartingCard(Point placement, StartingCard oldCard) {
        super(placement, oldCard);
        this.centralFrontResources = oldCard.centralFrontResources;
        this.frontCornersResources = oldCard.frontCornersResources;
    }

    @Override
    public Map<GameResource, Integer> getCardResources() {
        int[] resourcesCount = new int[7];

        if(!isFaceUp) {
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

        Hashtable<GameResource, Integer> countedResources = new Hashtable<>();
        for (GameResource r : GameResource.values()){
            countedResources.put(r, resourcesCount[r.getResourceIndex()]);
        }

        return countedResources;
    }

    @Override
    public GameResource getCardColour() {
        return null;
    }

    @Override
    public PlaceableCard setPosition(Point placement) {
        return new StartingCard(placement, this);
    }
}
