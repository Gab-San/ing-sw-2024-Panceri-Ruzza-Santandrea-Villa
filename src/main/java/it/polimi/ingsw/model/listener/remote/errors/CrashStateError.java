package it.polimi.ingsw.model.listener.remote.errors;

import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

public class CrashStateError extends RemoteErrorEvent{
    private final String crashMsg;

    public CrashStateError(String notifiedClient, String crashMsg) {
        super(notifiedClient);
        this.crashMsg = crashMsg;
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.reportError(crashMsg);
    }
}
