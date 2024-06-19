package it.polimi.ingsw.model.listener.remote.events.deck;

import it.polimi.ingsw.model.listener.remote.NetworkEvent;
import it.polimi.ingsw.model.listener.remote.events.UpdateEvent;

/**
 * This class represents an update event. An abstraction of an event triggered by a deck modification.
 */
abstract public class DeckEvent implements UpdateEvent {
    /**
     * Deck type identifier.
     */
    protected final char deck;

    /**
     * Constructs deck event.
     * @param deck deck identifier
     */
    protected DeckEvent(char deck) {
        this.deck = deck;
    }
}
