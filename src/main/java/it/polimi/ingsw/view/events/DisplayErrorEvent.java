package it.polimi.ingsw.view.events;

import it.polimi.ingsw.view.SceneManager;
import it.polimi.ingsw.view.gui.GameGUI;
import it.polimi.ingsw.view.tui.TUI;

/**
 * This class represents an event triggered by an error notification.
 */
public class DisplayErrorEvent implements TUIEvent, GUIEvent {
    private final String errorMessage;
//DOCS add docs
    public DisplayErrorEvent(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public void displayEvent(TUI tui) {
        tui.showError(errorMessage);
    }

    @Override
    public void displayEvent(GameGUI gui) {
        gui.showError(errorMessage);
    }
}
