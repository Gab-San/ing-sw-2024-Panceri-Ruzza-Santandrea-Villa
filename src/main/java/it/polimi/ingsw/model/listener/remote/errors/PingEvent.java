package it.polimi.ingsw.model.listener.remote.errors;

import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

public class PingEvent extends RemoteErrorEvent {
    public PingEvent(String nickname){
        super(nickname);
    }
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.ping();
    }
}
