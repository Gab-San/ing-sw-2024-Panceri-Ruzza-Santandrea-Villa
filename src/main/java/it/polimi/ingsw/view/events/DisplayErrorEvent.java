package it.polimi.ingsw.view.events;

import it.polimi.ingsw.view.tui.TUI;

/**
 * This class represents an event triggered by an error notification.
 */
public class DisplayErrorEvent implements TUIEvent {
    private final String errorMessage;

    public DisplayErrorEvent(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public void displayEvent(TUI tui) {
        tui.showError(errorMessage);
    }
}
