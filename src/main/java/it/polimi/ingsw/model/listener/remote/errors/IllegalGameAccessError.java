package it.polimi.ingsw.model.listener.remote.errors;

import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

/**
 * This class is a remote event and reports about an illegal access error.
 */
public class IllegalGameAccessError extends RemoteErrorEvent {
    private final String errorMsg;

    /**
     * Constructs the illegal access error message.
     * @param userNickname notified user id
     * @param errorMsg error message
     */
    public IllegalGameAccessError(String userNickname, String errorMsg) {
        super(userNickname);
        this.errorMsg = "ILLEGAL ACCESS: " + errorMsg;
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.reportError(errorMsg);
    }
}
