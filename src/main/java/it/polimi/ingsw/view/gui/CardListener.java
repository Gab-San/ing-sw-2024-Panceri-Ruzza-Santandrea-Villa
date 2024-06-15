package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.view.model.cards.ViewPlaceableCard;

public interface CardListener {
    void setClickedCard(String cardID, GamePoint position, CornerDirection direction);
    void setSelectedCard(ViewPlaceableCard card);
}
