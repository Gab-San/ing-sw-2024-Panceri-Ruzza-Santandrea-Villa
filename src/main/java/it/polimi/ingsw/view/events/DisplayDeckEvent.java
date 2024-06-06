package it.polimi.ingsw.view.events;

import it.polimi.ingsw.view.tui.TUI;

public class DisplayDeckEvent implements TUIEvent{
    private final String message;

    public DisplayDeckEvent(String message) {
        this.message = message;
    }


    @Override
    public void displayEvent(TUI tui) {
        tui.showNotification(message);
    }
}
