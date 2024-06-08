package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.view.Scene;

import java.util.List;

/**
 * Default empty scene to always be able to receive notifications
 */
public class NotificationScene implements Scene {
    @Override
    public void display() {

    }

    @Override
    public void displayError(String error) {

    }

    @Override
    public void displayNotification(List<String> backlog) {

    }

    @Override
    public void displayChatMessage(List<String> backlog) {

    }

    @Override
    public void moveView(List<CornerDirection> cornerDirections) {

    }

    @Override
    public void setCenter(int row, int col) {

    }

    @Override
    public void setCenter(GamePoint center) {

    }
}
