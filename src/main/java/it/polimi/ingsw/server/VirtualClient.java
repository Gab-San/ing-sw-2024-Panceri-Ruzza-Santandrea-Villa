package it.polimi.ingsw.server;

import java.rmi.RemoteException;

//TODO: create the actual VirtualClient
public interface VirtualClient {
    //FIXME: [Ale] review use of ConnectionLostException for TCP (RemoteException is needed for RMI)
    void update() throws RemoteException, ConnectionLostException;
    void ping() throws RemoteException, ConnectionLostException;
}
