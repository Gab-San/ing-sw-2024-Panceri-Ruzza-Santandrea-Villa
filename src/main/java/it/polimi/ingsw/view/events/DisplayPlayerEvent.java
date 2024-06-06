package it.polimi.ingsw.view.events;

import it.polimi.ingsw.view.tui.TUI;

public class DisplayPlayerEvent implements TUIEvent{
    private final String msg;

    public DisplayPlayerEvent(String msg) {
        this.msg = msg;
    }


    @Override
    public void displayEvent(TUI tui) {
        tui.showNotification(msg);
    }
}
