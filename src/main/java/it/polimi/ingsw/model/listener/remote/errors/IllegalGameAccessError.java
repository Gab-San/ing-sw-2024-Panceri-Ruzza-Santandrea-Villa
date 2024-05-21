package it.polimi.ingsw.model.listener.remote.errors;

import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

public class IllegalGameAccessError extends RemoteErrorEvent {
    private final String errorMsg;
    public IllegalGameAccessError(String userNickname, String errorMsg) {
        super(userNickname);
        this.errorMsg = "ILLEGAL ACCESS: " + errorMsg;
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.reportError(errorMsg);
    }
}
