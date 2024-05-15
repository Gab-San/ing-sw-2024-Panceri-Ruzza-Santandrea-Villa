package it.polimi.ingsw.server.tcp.message;

import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.tcp.message.TCPMessage;

import java.rmi.RemoteException;

public interface TCPServerMessage extends TCPMessage {
    void execute(VirtualClient virtualClient) throws RemoteException;
}
