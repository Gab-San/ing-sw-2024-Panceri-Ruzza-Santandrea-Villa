package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.CornerDirection;

public interface UI_Printer {
    void printUI();
    void moveView(CornerDirection ...cornerDirections);
    void setCenter(int row, int col);
}
