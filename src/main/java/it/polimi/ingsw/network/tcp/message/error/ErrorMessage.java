package it.polimi.ingsw.network.tcp.message.error;

import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.tcp.message.TCPServerMessage;

import java.rmi.RemoteException;

public class ErrorMessage implements TCPServerMessage {
    private final String errorMsg;

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
