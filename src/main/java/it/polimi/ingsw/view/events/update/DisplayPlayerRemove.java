package it.polimi.ingsw.view.events.update;

import it.polimi.ingsw.view.events.DisplayPlayerEvent;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.tui.TUI;

/**
 * This event handles player removal update.
 */
public class DisplayPlayerRemove extends DisplayPlayerEvent {
    /**
     * Constructs player removal event.
     *
     * @param nickname player's nickname who caused event to be triggered
     */
    public DisplayPlayerRemove(String nickname) {
        super(nickname, false);
    }

    @Override
    public void displayEvent(GUI gui) {

    }

    @Override
    public void displayEvent(TUI tui) {
        tui.showNotification(nickname + " has been removed.");
    }
}
