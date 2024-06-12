package it.polimi.ingsw.view.events.update;

import it.polimi.ingsw.view.events.DisplayPlayerEvent;
import it.polimi.ingsw.view.tui.TUI;

/**
 * This class represents an event triggered by a player becoming deadlocked.
 * <p>
 *     A deadlocked player can't place cards anymore.
 * </p>
 */
public class DisplayDeadLock extends DisplayPlayerEvent {
    /**
     * Constructs player deadlock event.
     *
     * @param nickname      player's nickname who caused event to be triggered
     * @param isLocalPlayer true if the event was triggered due to local player action, false otherwise.
     */
    public DisplayDeadLock(String nickname, boolean isLocalPlayer) {
        super(nickname, isLocalPlayer);
    }


    @Override
    public void displayEvent(TUI tui) {
        if(isLocalPlayer) {
            tui.showNotification("You are deadlocked!");
            return;
        }
        tui.showNotification(nickname + " is deadlocked!");
    }
}
