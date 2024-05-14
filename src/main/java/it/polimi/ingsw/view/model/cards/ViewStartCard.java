package it.polimi.ingsw.view.model.cards;

import java.util.List;

public class ViewStartCard extends ViewPlaceableCard {
    public ViewStartCard(String cardID, List<ViewCorner> corners) throws IllegalArgumentException {
        super(cardID, corners);
    }
}
