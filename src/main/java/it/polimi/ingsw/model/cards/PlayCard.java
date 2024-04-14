package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Point;
import it.polimi.ingsw.model.enums.GameResource;
import it.polimi.ingsw.model.PlayArea;
import it.polimi.ingsw.model.functions.UsefulFunc;

import java.security.InvalidParameterException;
import java.util.*;

public abstract class PlayCard extends PlaceableCard{
    private GameResource backResource;
    protected int pointsOnPlace;
    protected PlayCard(GameResource backResource, Corner... corners){
        super(corners);
        this.backResource = backResource;
        this.pointsOnPlace = 0;
    }
    protected PlayCard(GameResource backResource, int pointsOnPlace, Corner... corners) throws InvalidParameterException {
        super(corners);
        this.backResource = backResource;
        this.pointsOnPlace = pointsOnPlace;
    }

    protected PlayCard(Point placement, PlayCard oldCard){
        super(placement, oldCard);
        this.backResource = oldCard.backResource;
        this.pointsOnPlace = oldCard.pointsOnPlace;
    }
    public boolean equals(PlayCard other){
        return super.equals(other) &&
                getCardColour() == other.getCardColour() &&
                pointsOnPlace == other.pointsOnPlace &&
                getPlacementCost().equals(other.getPlacementCost());
    }
    @Override
    public boolean equals(Object other){
        if (other == this) return true;
        if(other instanceof PlayCard)
            return equals((PlayCard) other);
        else
            return false;
    }

    @Override
    public Map<GameResource, Integer> getCardResources() {
        int[] resourcesCount = super.getCornerResources();
        if(!isFaceUp){
            resourcesCount[backResource.getResourceIndex()]++;
        }

        return UsefulFunc.resourceArrayToMap(resourcesCount);
    }

    @Override
    public GameResource getCardColour() {
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
