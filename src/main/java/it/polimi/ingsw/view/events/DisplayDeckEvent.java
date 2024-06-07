package it.polimi.ingsw.view.events;

/**
 * This class represents an event dealing with a deck
 */
abstract public class DisplayDeckEvent implements TUIEvent {
    /**
     * Deck initial letter that acts as identifier.
     */
    protected final char deck;

    /**
     * Constructs deck event.
     * @param deck deck name initial
     */
    protected DisplayDeckEvent(char deck) {
        this.deck = deck;
    }
}
