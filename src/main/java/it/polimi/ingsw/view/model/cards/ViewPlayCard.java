package it.polimi.ingsw.view.model.cards;

import it.polimi.ingsw.view.model.enums.GameResourceView;

import java.util.List;
import java.util.stream.Collectors;

public abstract class ViewPlayCard extends ViewPlaceableCard{
    protected final int pointsOnPlace;
    private final GameResourceView backResource;

    public ViewPlayCard(String cardID, String imageFrontName, String imageBackName, List<ViewCorner> corners, int pointsOnPlace, GameResourceView backResource) {
        super(cardID, imageFrontName, imageBackName, corners);
        this.pointsOnPlace = pointsOnPlace;
        this.backResource = backResource;
    }

    @Override
    public GameResourceView getCardColour() {
        return backResource;
    }
    public String getPointsOnPlaceAsString() {
        return pointsOnPlace > 0 ? Integer.toString(pointsOnPlace) : "";
    }
    abstract public String getPlacementCostAsString();
}
