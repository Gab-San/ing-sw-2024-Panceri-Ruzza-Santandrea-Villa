package it.polimi.ingsw.network.tcp.message.error;

import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.tcp.message.TCPServerMessage;

import java.rmi.RemoteException;

public class DisconnectErrorMessage implements TCPServerMessage {
    @Override
    public boolean isCheck() {
        return false;
    }

    @Override
    public void execute(VirtualClient virtualClient) throws RemoteException {
        virtualClient.notifyTimeoutDisconnect();
    }
}
