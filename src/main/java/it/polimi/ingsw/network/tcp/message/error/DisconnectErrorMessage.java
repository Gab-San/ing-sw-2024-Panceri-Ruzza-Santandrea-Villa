package it.polimi.ingsw.network.tcp.message.error;

import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.tcp.message.TCPServerMessage;

import java.io.Serial;
import java.rmi.RemoteException;

/**
 * This class implements tcp server message interface.
 * Used to notify a forced disconnection.
 */
public class DisconnectErrorMessage implements TCPServerMessage {
    @Serial
    private static final long serialVersionUID = 513138463514L;
    @Override
    public boolean isCheck() {
        return false;
    }

    @Override
    public void execute(VirtualClient virtualClient) throws RemoteException {
        virtualClient.notifyIndirectDisconnect();
    }
}
