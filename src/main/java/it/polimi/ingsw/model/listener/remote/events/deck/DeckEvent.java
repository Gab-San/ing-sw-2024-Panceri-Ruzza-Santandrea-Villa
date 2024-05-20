package it.polimi.ingsw.model.listener.remote.events.deck;

import it.polimi.ingsw.model.listener.remote.NetworkEvent;

abstract public class DeckEvent implements NetworkEvent {
    protected final char deck;
    protected DeckEvent(char deck) {
        this.deck = deck;
    }
}
