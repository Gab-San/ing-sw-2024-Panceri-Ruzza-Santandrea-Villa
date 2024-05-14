package it.polimi.ingsw.view.model.cards;

import java.util.List;

public class ViewResourceCard extends ViewPlayCard {

    public ViewResourceCard(String cardID, List<ViewCorner> corners, int pointsOnPlace) throws IllegalArgumentException {
        super(cardID, corners, pointsOnPlace);
    }
}
