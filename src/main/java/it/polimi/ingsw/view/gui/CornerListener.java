package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GamePoint;

public interface CornerListener {
    void setClickedCard(String cardID, GamePoint position, CornerDirection direction) throws IllegalStateException;
}
