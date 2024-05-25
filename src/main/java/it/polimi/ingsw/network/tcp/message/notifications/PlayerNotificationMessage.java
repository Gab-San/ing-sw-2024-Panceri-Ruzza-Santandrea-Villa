package it.polimi.ingsw.network.tcp.message.notifications;

import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.tcp.message.TCPServerMessage;

import java.rmi.RemoteException;

abstract public class PlayerNotificationMessage implements TCPServerMessage {
    protected final String nickname;

    protected PlayerNotificationMessage(String nickname) {
        this.nickname = nickname;
    }
}
