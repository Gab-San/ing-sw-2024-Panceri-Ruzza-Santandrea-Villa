package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Point;
import it.polimi.ingsw.model.enums.*;
import it.polimi.ingsw.model.functions.UsefulFunc;

import java.security.InvalidParameterException;
import java.util.*;

/**
 * This represents the starting card: the first card placed in the setup phase.
 */
public class StartingCard extends PlaceableCard {
    private final List<GameResource> centralFrontResources;

    /**
     * Constructs a starting card filled with the required information.
     * @param centralRes The resources shown in the center of the card
     * @param corners The corners of the card
     */
    public StartingCard(GameResource[] centralRes, Corner... corners) throws InvalidParameterException {
        super(corners);

        this.centralFrontResources = new ArrayList<>();
        centralFrontResources.addAll(Arrays.asList(centralRes));
    }

    /**
     * Constructs a starting card from another one, adding the placement information.
     * @param placement The placement point of the card (starting cards are always placed at (0,0))
     * @param oldCard The unplaced card which is copied
     */
     private StartingCard(Point placement, StartingCard oldCard) {
        super(placement, oldCard);
        this.centralFrontResources = oldCard.centralFrontResources;
    }
    public boolean equals(StartingCard other){
         return super.equals(other) &&
                 centralFrontResources.equals(other.centralFrontResources);
    }
    @Override
    public boolean equals(Object other){
        if (other == this) return true;
        if(other instanceof StartingCard)
            return equals((StartingCard) other);
        else
            return false;
    }

    @Override
    public Map<GameResource, Integer> getCardResources() {
        int[] resourcesCount = super.getCornerResources();
        if(isFaceUp) {
            for(GameResource res: centralFrontResources){
                resourcesCount[res.getResourceIndex()]++;
            }
        }

        return UsefulFunc.resourceArrayToMap(resourcesCount);
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
