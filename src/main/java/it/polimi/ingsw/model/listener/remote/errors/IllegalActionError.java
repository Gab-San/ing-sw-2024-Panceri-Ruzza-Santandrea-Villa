package it.polimi.ingsw.model.listener.remote.errors;

import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

/**
 * This class is a remote error event and reports about an illegal action.
 */
public class IllegalActionError extends RemoteErrorEvent{
    private final String errorMessage;

    /**
     * Constructs the illegal action error.
     * @param userNickname user id to be notified
     * @param errorMessage error message
     */
    public IllegalActionError(String userNickname, String errorMessage) {
        super(userNickname);
        this.errorMessage = "ILLEGAL ACTION: " + errorMessage;
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.reportError(errorMessage);
    }
}
