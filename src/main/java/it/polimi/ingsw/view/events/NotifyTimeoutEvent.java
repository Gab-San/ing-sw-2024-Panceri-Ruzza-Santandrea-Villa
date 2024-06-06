package it.polimi.ingsw.view.events;

import it.polimi.ingsw.view.tui.TUI;

public class NotifyTimeoutEvent implements TUIEvent{

    @Override
    public void displayEvent(TUI tui) {
        tui.notifyTimeout();
    }
}
