package it.polimi.ingsw.model.listener.remote.errors;

import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

public class IllegalParameterError extends RemoteErrorEvent{
    private final String errorMsg;
    public IllegalParameterError(String userNickname, String errorMsg) {
        super(userNickname);
        this.errorMsg = "ILLEGAL ARGUMENT: " + errorMsg;
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {

    }
}
