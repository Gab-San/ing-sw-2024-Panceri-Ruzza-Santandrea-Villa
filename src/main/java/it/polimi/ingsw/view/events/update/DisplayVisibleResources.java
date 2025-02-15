package it.polimi.ingsw.view.events.update;

import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.view.events.DisplayPlayerEvent;
import it.polimi.ingsw.view.tui.TUI;

import java.util.Map;

/**
 * This class handles visible resources updates.
 */
public class DisplayVisibleResources extends DisplayPlayerEvent {

    /**
     * Constructs player event.
     *
     * @param nickname      player's nickname who caused event to be triggered
     * @param isLocalPlayer true if the event was triggered due to local player action, false otherwise.
     */
    public DisplayVisibleResources(String nickname, boolean isLocalPlayer) {
        super(nickname, isLocalPlayer);
    }

    @Override
    public void displayEvent(TUI tui) {
        tui.setRefreshTimer();
    }
}
