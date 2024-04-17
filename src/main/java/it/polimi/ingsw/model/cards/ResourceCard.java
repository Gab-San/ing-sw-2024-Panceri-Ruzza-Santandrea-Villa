package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Point;
import it.polimi.ingsw.model.enums.GameResource;
import it.polimi.ingsw.model.PlayArea;

import java.security.InvalidParameterException;
import java.util.*;

public class ResourceCard extends PlayCard{
    public ResourceCard() {
        super();
    }
    public ResourceCard(GameResource backResource, Corner... corners) throws InvalidParameterException {
        super(backResource, corners);
    }
    public ResourceCard(GameResource backResource, int pointsOnPlace, Corner... corners) throws InvalidParameterException {
        super(backResource, pointsOnPlace, corners);
    }

    private ResourceCard(Point placement, ResourceCard oldCard){
        super(placement, oldCard);
    }

    /**
     * @return empty Map (Resource cards have no placement cost)
     */
    @Override
    public Map<GameResource, Integer> getPlacementCost() {
        return new Hashtable<>();
    }

    @Override
    public int calculatePointsOnPlace(PlayArea playArea) {
        return pointsOnPlace;
    }

    @Override
    public PlaceableCard setPosition(Point placement) {
        return new ResourceCard(placement, this);
    }
}
