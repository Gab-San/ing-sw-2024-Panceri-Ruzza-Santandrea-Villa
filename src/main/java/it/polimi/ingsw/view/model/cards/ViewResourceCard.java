package it.polimi.ingsw.view.model.cards;

import it.polimi.ingsw.GameResource;

import java.util.LinkedList;
import java.util.List;

public class ViewResourceCard extends ViewPlayCard {

    public ViewResourceCard(String cardID, String imageFrontName, String imageBackName, List<ViewCorner> corners, int pointsOnPlace, GameResource backResource) {
        super(cardID, imageFrontName, imageBackName, corners, pointsOnPlace, backResource);
    }
    public ViewResourceCard(ViewResourceCard other){
        super(other);
    }

    @Override
    public String getPlacementCostAsString() {
        return "";
    }

    @Override
    public List<GameResource> getPlacementCost() {
        return new LinkedList<>();
    }
}
