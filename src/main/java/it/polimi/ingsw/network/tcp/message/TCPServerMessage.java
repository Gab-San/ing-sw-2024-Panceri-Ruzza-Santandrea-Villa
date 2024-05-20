package it.polimi.ingsw.network.tcp.message;

import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

public interface TCPServerMessage extends TCPMessage {
    void execute(VirtualClient virtualClient) throws RemoteException;
}
