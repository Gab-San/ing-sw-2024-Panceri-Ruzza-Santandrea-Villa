package it.polimi.ingsw.view.events;
import it.polimi.ingsw.view.gui.GameGUI;

/**
 * This interface defines gui events.
 */
public interface GUIEvent extends DisplayEvent {
    /**
     * Displays event on gui.
     * @param gui gui on which to display event
     */
    void displayEvent(GameGUI gui);
}
