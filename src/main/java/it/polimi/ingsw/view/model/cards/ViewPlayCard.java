package it.polimi.ingsw.view.model.cards;

import java.util.List;

public class ViewPlayCard extends ViewPlaceableCard{
    final int pointsOnPlace;

    public ViewPlayCard(String cardID, List<ViewCorner> corners, int pointsOnPlace) throws IllegalArgumentException {
        super(cardID, corners);
        this.pointsOnPlace = pointsOnPlace;
    }
}
