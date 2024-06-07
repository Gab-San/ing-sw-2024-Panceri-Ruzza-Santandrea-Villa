package it.polimi.ingsw.view.events;

import it.polimi.ingsw.view.gui.GUI;

/**
 * This interface defines gui events.
 */
public interface GUIEvent extends DisplayEvent {
    /**
     * Displays event on given gui.
     * @param gui gui on which to display event
     */
    void displayEvent(GUI gui);
}
