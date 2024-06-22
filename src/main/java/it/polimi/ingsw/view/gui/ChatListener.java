package it.polimi.ingsw.view.gui;

/**
 * This interface defines the methods of an object that listens to chat events.
 */
public interface ChatListener {
    /**
     * Displays the message on the chat.
     * @param messenger identifier of the user which sent the message (eventually the server)
     * @param message message text
     */
    void displayMessage(String messenger, String message);
}
