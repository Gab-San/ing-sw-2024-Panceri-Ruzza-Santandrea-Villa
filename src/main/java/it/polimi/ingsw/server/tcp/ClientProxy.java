package it.polimi.ingsw.server.tcp;

import it.polimi.ingsw.server.VirtualClient;

import java.rmi.RemoteException;

public class ClientProxy implements VirtualClient {
    @Override
    public void update(String msg) throws RemoteException {

    }

    @Override
    public void ping() throws RemoteException {

    }
}
