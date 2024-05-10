package it.polimi.ingsw.server.tcp.message;

import it.polimi.ingsw.server.VirtualClient;

import java.rmi.RemoteException;

public interface TCPServerMessage extends TCPMessage{
    void execute(VirtualClient virtualClient) throws RemoteException;
}
