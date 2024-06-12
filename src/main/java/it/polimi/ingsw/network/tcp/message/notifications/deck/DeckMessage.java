package it.polimi.ingsw.network.tcp.message.notifications.deck;

import it.polimi.ingsw.network.tcp.message.TCPServerMessage;

/**
 * This class implements the tcp server message interface. It represents
 * all messages dealing with decks.
 */
abstract public class DeckMessage implements TCPServerMessage {
    private static final long serialVersionUID = 1842709812475L;
    /**
     * Single letter unique identifier of the deck.
     * R - Resource <br>
     * G - Gold <br>
     * O - Objective <br>
     */
    protected final char deck;

    /**
     * Constructs the general deck message.
     * @param deck unique identifier of the deck
     */
    protected DeckMessage(char deck) {
        this.deck = deck;
    }

    @Override
    public boolean isCheck() {
        return false;
    }
}
