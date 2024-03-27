package it.polimi.ingsw.model.cards;
import it.polimi.ingsw.model.enums.GameResource;

public class ResourceCard extends PlayCard{
    @Override
    Map<GameResource, Integer> getPlacementCost() {
        return null;
    }

    @Override
    int calculatePointsOnPlace(PlayArea pA) {
        return pointsOnPlace;
    }

    @Override
    public GameResource getCardColor() {
        return backResource;
    }
}
