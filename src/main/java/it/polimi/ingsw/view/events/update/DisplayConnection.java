package it.polimi.ingsw.view.events.update;

import it.polimi.ingsw.view.events.DisplayPlayerEvent;
import it.polimi.ingsw.view.events.TUIEvent;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.tui.TUI;

//DOCS [Gamba] add docs
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

    @Override
    public void displayEvent(GUI gui) {

    }
}
