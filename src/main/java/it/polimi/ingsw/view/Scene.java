package it.polimi.ingsw.view;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.Point;

import java.util.List;

public interface Scene {
    void display();
    void displayError(String error);
    void displayNotification(List<String> backlog);
    void displayChatMessage(List<String> backlog);
    void moveView(List<CornerDirection> cornerDirections);
    void setCenter(int row, int col);
    void setCenter(Point center);
}