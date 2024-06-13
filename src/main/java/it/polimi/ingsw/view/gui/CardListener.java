package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.CornerDirection;

public interface CardListener {
    void setClickedCard(String cardID, int x, int y, CornerDirection direction);
}
