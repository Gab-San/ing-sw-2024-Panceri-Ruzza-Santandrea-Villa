package it.polimi.ingsw.model.listener.remote.errors;

import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

/**
 * This class is a remote error and represents an illegal parameter error.
 */
public class IllegalParameterError extends RemoteErrorEvent{
    private final String errorMsg;

    /**
     * Constructs an illegal parameter error message.
     * @param userNickname notified user id
     * @param errorMsg error message
     */
    public IllegalParameterError(String userNickname, String errorMsg) {
        super(userNickname);
        this.errorMsg = "ILLEGAL ARGUMENT: " + errorMsg;
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.reportError(errorMsg);
    }
}
