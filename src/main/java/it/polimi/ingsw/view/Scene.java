package it.polimi.ingsw.view;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GamePoint;

import java.util.List;

public interface Scene {
    /**
     * Displays the scene on-screen.
     */
    void display();

    /**
     * Displays an error message on this scene
     * @param error the error message
     */
    void displayError(String error);

    /**
     * Displays a notification on this scene
     * @param backlog the notification backlog
     */
    void displayNotification(List<String> backlog);

    // FIXME: Strong suspect that it is not correct leaving these here
    void moveView(List<CornerDirection> cornerDirections);
    void setCenter(int row, int col);
    void setCenter(GamePoint center);
}