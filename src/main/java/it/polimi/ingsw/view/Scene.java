package it.polimi.ingsw.view;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.Point;

public interface Scene {
    void display();
    void displayError(String msg);
    void displayNotification(String msg);
    void moveView(CornerDirection ...cornerDirections);
    void setCenter(int row, int col);
    void setCenter(Point center);
}