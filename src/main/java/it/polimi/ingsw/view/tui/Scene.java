package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.Point;

public interface Scene {
    void display();
    void moveView(CornerDirection ...cornerDirections);
    void setCenter(int row, int col);
    void setCenter(Point center);
}