package it.polimi.ingsw.model.listener.remote.errors;

import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

public class IllegalStateError extends RemoteErrorEvent{
    private final String errorMessage;
    public IllegalStateError(String userNickname, String errorMessage) {
        super(userNickname);
        this.errorMessage = "ILLEGAL EXECUTION STATE: " + errorMessage;
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {

    }
}
