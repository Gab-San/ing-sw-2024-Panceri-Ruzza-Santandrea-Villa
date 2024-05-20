package it.polimi.ingsw.view;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.Point;

import java.io.PrintWriter;

import static it.polimi.ingsw.view.tui.ConsoleTextColors.RED_TEXT;
import static it.polimi.ingsw.view.tui.ConsoleTextColors.RESET;

public interface Scene {
    void display();
    void displayError(String msg);
    void moveView(CornerDirection ...cornerDirections);
    void setCenter(int row, int col);
    void setCenter(Point center);
}