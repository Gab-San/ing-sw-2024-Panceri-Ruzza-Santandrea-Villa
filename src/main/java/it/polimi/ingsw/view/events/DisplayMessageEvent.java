package it.polimi.ingsw.view.events;

import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.tui.TUI;

/**
 * This class handles messages events.
 */
public class DisplayMessageEvent implements TUIEvent, GUIEvent{
    private final String message;
    private final String messenger;
    public DisplayMessageEvent(String messenger, String message) {
        this.message = message;
        this.messenger = messenger;
    }

    @Override
    public void displayEvent(TUI tui) {
        tui.showChatMessage(messenger, message);
    }

    @Override
    public void displayEvent(GUI gui) {
        gui.showChatMessage(messenger, message);
    }
}
