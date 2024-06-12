package it.polimi.ingsw.view.events.update;

import it.polimi.ingsw.view.events.DisplayPlayerEvent;
import it.polimi.ingsw.view.tui.TUI;

/**
 * This class represents a connection event. It notifies about the connection status of the specified player.
 */
public class DisplayConnection extends DisplayPlayerEvent {

    private final boolean isConnected;
    private final boolean changed;

    /**
     * Constructs player event.
     *
     * @param nickname      player's nickname who caused event to be triggered
     */
    public DisplayConnection(String nickname, boolean isConnected, boolean changed) {
        super(nickname, false);
        this.isConnected = isConnected;
        this.changed = changed;
    }

    @Override
    public void displayEvent(TUI tui) {
        tui.showNotification(nickname + " " +
                (isConnected ? (changed ? "reconnected" : "connected") : "disconnected"));
    }

}
