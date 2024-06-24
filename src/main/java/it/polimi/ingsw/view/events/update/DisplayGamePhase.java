package it.polimi.ingsw.view.events.update;

import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.view.events.GUIEvent;
import it.polimi.ingsw.view.events.TUIEvent;
import it.polimi.ingsw.view.gui.GameGUI;
import it.polimi.ingsw.view.tui.TUI;

/**
 * This class handles displaying of game phase updates.
 */
public class DisplayGamePhase implements TUIEvent, GUIEvent {
    private final GamePhase gamePhase;

    /**
     * Constructs game phase update event.
     * @param gamePhase updated game phase
     */
    public DisplayGamePhase(GamePhase gamePhase) {
        this.gamePhase = gamePhase;
    }

    @Override
    public void displayEvent(GameGUI gui) {
        gui.updatePhase(gamePhase);
    }

    @Override
    public void displayEvent(TUI tui) {
        tui.showNotification("Game phase updated to " + gamePhase);
    }
}
