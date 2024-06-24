package it.polimi.ingsw.view;

import java.util.List;

/**
 * This interface defines the skeleton of a displayable scene.
 */
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
}