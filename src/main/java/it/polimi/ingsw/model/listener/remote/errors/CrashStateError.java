package it.polimi.ingsw.model.listener.remote.errors;

import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

/**
 * This class is an error event that notifies about a crash.
 */
public class CrashStateError extends RemoteErrorEvent{
    private final String crashMsg;

    /**
     * Creates a crash event.
     * @param notifiedClient identifier of the client to be notified
     * @param crashMsg crash error message
     */
    public CrashStateError(String notifiedClient, String crashMsg) {
        super(notifiedClient);
        this.crashMsg = crashMsg;
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.reportError(crashMsg);
    }
}
