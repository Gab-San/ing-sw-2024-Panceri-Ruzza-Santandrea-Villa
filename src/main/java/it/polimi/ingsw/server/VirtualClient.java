package it.polimi.ingsw.server;

import java.rmi.RemoteException;

//TODO: create the actual VirtualClient
public abstract class VirtualClient {
    public abstract void update() throws RemoteException;
}
