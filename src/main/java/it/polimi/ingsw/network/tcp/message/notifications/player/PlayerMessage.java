package it.polimi.ingsw.network.tcp.message.notifications.player;

import it.polimi.ingsw.network.tcp.message.TCPServerMessage;

import java.io.Serial;

abstract public class PlayerMessage implements TCPServerMessage {
    @Serial
    private static final long serialVersionUID = 1482764941L;
    protected final String nickname;

    protected PlayerMessage(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public boolean isCheck() {
        return false;
    }
}
