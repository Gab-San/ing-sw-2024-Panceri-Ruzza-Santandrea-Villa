package it.polimi.ingsw.server.tcp.message;

import it.polimi.ingsw.server.tcp.client.ClientSideProxy;
import it.polimi.ingsw.server.tcp.message.TCPMessage;

import java.rmi.RemoteException;

public interface TCPServerCheckMessage extends TCPMessage {
    void handle(ClientSideProxy client) throws IllegalStateException, IllegalArgumentException;
}
