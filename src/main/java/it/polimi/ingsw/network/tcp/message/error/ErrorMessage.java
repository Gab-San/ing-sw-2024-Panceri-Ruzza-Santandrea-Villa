package it.polimi.ingsw.network.tcp.message.error;

import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.tcp.message.TCPServerMessage;

import java.io.Serial;
import java.rmi.RemoteException;

/**
 * This class implements tcp server message interface.
 * Sent when an exception is raised while executing the action.
 */
public class ErrorMessage implements TCPServerMessage {
    @Serial
    private static final long serialVersionUID = 879546354621L;
    private final String errorMsg;

    /**
     * Constructs the error message.
     * @param errorMsg error notification message
     */
    public ErrorMessage(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public void execute(VirtualClient virtualClient) throws RemoteException {
        virtualClient.reportError(errorMsg);
    }

    @Override
    public boolean isCheck() {
        return false;
    }
}
