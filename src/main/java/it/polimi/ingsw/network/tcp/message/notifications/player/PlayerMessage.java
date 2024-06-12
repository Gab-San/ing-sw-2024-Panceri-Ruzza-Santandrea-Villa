package it.polimi.ingsw.network.tcp.message.notifications.player;

import it.polimi.ingsw.network.tcp.message.TCPServerMessage;

import java.io.Serial;

/**
 * This class implements tcp server message interface.
 * It is teh base message for notifications that deal with player events.
 */
abstract public class PlayerMessage implements TCPServerMessage {
    @Serial
    private static final long serialVersionUID = 1482764941L;
    /**
     * Unique identifier of the user.
     */
    protected final String nickname;

    /**
     * Constructs the player message.
     * @param nickname unique id of the user
     */
    protected PlayerMessage(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public boolean isCheck() {
        return false;
    }
}
