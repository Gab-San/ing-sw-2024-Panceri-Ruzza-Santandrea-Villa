package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.view.model.cards.ViewPlaceableCard;

public interface CardListener {
    void setClickedCard(String cardID, int x, int y, CornerDirection direction);
    void setSelectedCard(ViewPlaceableCard card);
}
