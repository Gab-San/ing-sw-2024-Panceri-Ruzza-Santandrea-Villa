package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enums.GameResource;
import it.polimi.ingsw.model.PlayArea;

import java.util.*;

public class ResourceCard extends PlayCard{

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
}
