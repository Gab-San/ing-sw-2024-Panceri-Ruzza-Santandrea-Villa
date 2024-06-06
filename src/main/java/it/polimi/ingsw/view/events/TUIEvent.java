package it.polimi.ingsw.view.events;

import it.polimi.ingsw.view.tui.TUI;

public interface TUIEvent extends DisplayEvent{
    void displayEvent(TUI tui);
}
