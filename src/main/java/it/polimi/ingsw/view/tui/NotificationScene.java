package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.view.Scene;

import java.util.List;

/**
 * Default empty scene to always be able to receive notifications
 */
public class NotificationScene implements Scene {
    /**
     * Default constructor.
     */
    public NotificationScene(){}
    @Override
    public void display() {

    }

    @Override
    public void displayError(String error) {

    }

    @Override
    public void displayNotification(List<String> backlog) {

    }
}
