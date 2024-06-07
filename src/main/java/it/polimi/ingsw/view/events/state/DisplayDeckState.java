package it.polimi.ingsw.view.events.state;

import it.polimi.ingsw.view.events.DisplayDeckEvent;
import it.polimi.ingsw.view.events.TUIEvent;
import it.polimi.ingsw.view.model.ViewDeck;
import it.polimi.ingsw.view.model.cards.ViewCard;
import it.polimi.ingsw.view.tui.TUI;

/**
 * This class represents an event triggered by a deck state update.
 */
public class DisplayDeckState extends DisplayDeckEvent {

    private final ViewCard topCard;
    private final ViewCard firstRevCard;
    private final ViewCard secondRevCard;

    /**
     * Constructs deck state event.
     * @param deck deck name initial
     * @param topCard top card
     * @param firstRevCard first revealed card
     * @param secondRevCard second revealed card
     */
    public DisplayDeckState(char deck, ViewCard topCard, ViewCard firstRevCard, ViewCard secondRevCard) {
        super(deck);
        this.topCard = topCard;
        this.firstRevCard = firstRevCard;
        this.secondRevCard = secondRevCard;
    }

    @Override
    public void displayEvent(TUI tui) {
        tui.showNotification(ViewDeck.getDeckName(deck) + " was updated.");
    }
}
