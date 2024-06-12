package it.polimi.ingsw.view.events.update;

import it.polimi.ingsw.view.events.TUIEvent;
import it.polimi.ingsw.view.tui.TUI;

/**
 * This class represents a turn event update.
 */
public class DisplayTurn implements TUIEvent{
    private final int turn;

    /**
     * Constructs turn event.
     * @param turn updated turn
     */
    public DisplayTurn(int turn) {
        this.turn = turn;
    }


    @Override
    public void displayEvent(TUI tui) {
        tui.showNotification("Turn advanced to " + turn);
    }
}
