package it.polimi.ingsw.view.model.cards;

import it.polimi.ingsw.GameResource;

import java.util.List;

public abstract class ViewPlayCard extends ViewPlaceableCard{
    protected final int pointsOnPlace;
    private final GameResource backResource;

    public ViewPlayCard(String cardID, String imageFrontName, String imageBackName, List<ViewCorner> corners, int pointsOnPlace, GameResource backResource) {
        super(cardID, imageFrontName, imageBackName, corners);
        this.pointsOnPlace = pointsOnPlace;
        this.backResource = backResource;
    }

    @Override
    public GameResource getCardColour() {
        return backResource;
    }
    public String getPointsOnPlaceAsString() {
        if(!isFaceUp) return "";
        else return pointsOnPlace > 0 ? Integer.toString(pointsOnPlace) : "";
    }
    abstract public String getPlacementCostAsString();
}
