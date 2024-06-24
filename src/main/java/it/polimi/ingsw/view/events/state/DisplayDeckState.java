package it.polimi.ingsw.view.events.state;

import it.polimi.ingsw.view.events.DisplayDeckEvent;
import it.polimi.ingsw.view.model.ViewDeck;
import it.polimi.ingsw.view.tui.TUI;

/**
 * This class represents an event triggered by a deck state update.
 */
public class DisplayDeckState extends DisplayDeckEvent {


    /**
     * Constructs deck state event.
     * @param deck deck name initial
     */
    public DisplayDeckState(char deck) {
        super(deck);
    }

    @Override
    public void displayEvent(TUI tui) {
        tui.showNotification(ViewDeck.getDeckName(deck) + " was updated.");
    }
}
