package it.polimi.ingsw.network.tcp.message.notifications.deck;

import it.polimi.ingsw.network.tcp.message.TCPServerMessage;

abstract public class DeckMessage implements TCPServerMessage {
    private static final long serialVersionUID = 1842709812475L;
    protected final char deck;

    protected DeckMessage(char deck) {
        this.deck = deck;
    }

    @Override
    public boolean isCheck() {
        return false;
    }
}
