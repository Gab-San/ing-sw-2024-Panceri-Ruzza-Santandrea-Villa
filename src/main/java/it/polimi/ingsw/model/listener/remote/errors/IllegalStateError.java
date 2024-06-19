package it.polimi.ingsw.model.listener.remote.errors;

import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

/**
 * This class represents a remote error event and reports about an illegal state.
 */
public class IllegalStateError extends RemoteErrorEvent{
    private final String errorMessage;

    /**
     * Constructs the illegal state error.
     * @param userNickname notified user id
     * @param errorMessage error message
     */
    public IllegalStateError(String userNickname, String errorMessage) {
        super(userNickname);
        this.errorMessage = "ILLEGAL EXECUTION STATE: " + errorMessage;
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.reportError(errorMessage);
    }
}
