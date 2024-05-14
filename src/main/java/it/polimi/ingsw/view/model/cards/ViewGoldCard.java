package it.polimi.ingsw.view.model.cards;

import java.util.List;

public class ViewGoldCard extends ViewPlayCard{

    public ViewGoldCard(String cardID, List<ViewCorner> corners, int pointsOnPlace) throws IllegalArgumentException {
        super(cardID, corners, pointsOnPlace);
    }
}
