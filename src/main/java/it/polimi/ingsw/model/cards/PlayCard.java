package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enums.GameResource;

public abstract class PlayCard extends PlaceableCard{
    GameResource backResource;
    int pointsOnPlace;
    // FIXME:
    //  - decide which map class is better
    //  - implement all inherited methods
    abstract Map<GameResource, Integer> getPlacementCost();
    abstract int calculatePointsOnPlace(PlayArea pA);
}
