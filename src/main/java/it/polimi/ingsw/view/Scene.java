package it.polimi.ingsw.view;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GamePoint;

import java.util.List;

public interface Scene {
    void display();
    void displayError(String error);
    void displayNotification(List<String> backlog);
    void displayChatMessage(List<String> backlog);

    // FIXME: Strong suspect that it is not correct leaving these here
    void moveView(List<CornerDirection> cornerDirections);
    void setCenter(int row, int col);
    void setCenter(GamePoint center);
}