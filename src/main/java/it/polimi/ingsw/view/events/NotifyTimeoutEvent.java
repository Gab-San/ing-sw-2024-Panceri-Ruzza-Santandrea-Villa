package it.polimi.ingsw.view.events;

import it.polimi.ingsw.view.gui.GameGUI;
import it.polimi.ingsw.view.tui.TUI;

/**
 * This class handles indirect disconnection by timeout event.
 */
public class NotifyTimeoutEvent implements TUIEvent, GUIEvent{
    /**
     * Default constructor.
     */
    public NotifyTimeoutEvent(){}
    @Override
    public void displayEvent(TUI tui) {
        tui.notifyTimeout();
    }

    @Override
    public void displayEvent(GameGUI gui) {
        gui.notifyTimeout();
    }
}
