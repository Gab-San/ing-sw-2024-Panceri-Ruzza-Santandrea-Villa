package it.polimi.ingsw.model.listener.remote.errors;

import it.polimi.ingsw.network.CentralServer;
import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

public class IndirectDisconnectEvent extends RemoteErrorEvent {
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
