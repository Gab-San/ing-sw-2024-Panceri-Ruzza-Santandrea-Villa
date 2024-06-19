package it.polimi.ingsw.model.listener.remote.errors;

import it.polimi.ingsw.network.CentralServer;
import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

/**
 * This class is a remote error event and reports about an indirect disconnection event.
 * <p>
 *     A disconnection is said to be indirect when it doesn't occur following a
 *     user input. Cases are timeout depletion and connection errors.
 * </p>
 */
public class IndirectDisconnectEvent extends RemoteErrorEvent {
    /**
     * Construct the indirect disconnection event.
     * @param nickname disconnected user id
     */
    public IndirectDisconnectEvent(String nickname) {
        super(nickname);
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        try {
            CentralServer.getSingleton().disconnect(notifiedClient, virtualClient);
        } catch (IllegalStateException | IllegalArgumentException ignore){}

        virtualClient.notifyIndirectDisconnect();
    }
}
