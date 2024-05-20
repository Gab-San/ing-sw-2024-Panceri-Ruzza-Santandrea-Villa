package it.polimi.ingsw.model.listener.remote.errors;

import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

public class IllegalActionError extends RemoteErrorEvent{
    private final String errorMessage;
    public IllegalActionError(String userNickname, String errorMessage) {
        super(userNickname);
        this.errorMessage = "ILLEGAL ACTION: " + errorMessage;
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {

    }
}
