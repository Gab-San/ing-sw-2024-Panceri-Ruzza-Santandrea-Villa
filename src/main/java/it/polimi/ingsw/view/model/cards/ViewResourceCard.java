package it.polimi.ingsw.view.model.cards;

import it.polimi.ingsw.GameResource;

import java.util.List;

public class ViewResourceCard extends ViewPlayCard {

    public ViewResourceCard(String cardID, String imageFrontName, String imageBackName, List<ViewCorner> corners, int pointsOnPlace, GameResource backResource) {
        super(cardID, imageFrontName, imageBackName, corners, pointsOnPlace, backResource);
    }

    @Override
    public String getPlacementCostAsString() {
        return "";
    }
}
