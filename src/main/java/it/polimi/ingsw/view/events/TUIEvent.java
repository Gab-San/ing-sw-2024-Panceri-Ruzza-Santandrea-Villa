package it.polimi.ingsw.view.events;

import it.polimi.ingsw.view.tui.TUI;

/**
 * This interface defines tui events.
 */
public interface TUIEvent extends DisplayEvent{
    /**
     * Displays event on tui.
     * @param tui tui on which to display event.
     */
    void displayEvent(TUI tui);
}
