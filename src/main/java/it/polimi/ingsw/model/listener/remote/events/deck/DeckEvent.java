package it.polimi.ingsw.model.listener.remote.events.deck;

import it.polimi.ingsw.model.listener.remote.NetworkEvent;
import it.polimi.ingsw.model.listener.remote.events.UpdateEvent;

abstract public class DeckEvent implements UpdateEvent {
    protected final char deck;
    protected DeckEvent(char deck) {
        this.deck = deck;
    }
}
