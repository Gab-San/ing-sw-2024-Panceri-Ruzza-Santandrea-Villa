package it.polimi.ingsw.model.cards;
import it.polimi.ingsw.model.enums.GameResource;

import java.util.Hashtable;

public class ResourceCard extends PlayCard{

    //TODO Should throw an exception or return null?
    @Override
    Hashtable<GameResource, Integer> getPlacementCost() {
        return null;
    }

    @Override
    int calculatePointsOnPlace(PlayArea playArea) {
        return pointsOnPlace;
    }

    @Override
    public GameResource getCardColor() {
        return backResource;
    }
}
