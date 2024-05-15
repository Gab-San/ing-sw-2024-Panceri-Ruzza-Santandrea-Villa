package it.polimi.ingsw.server.tcp.message.checkMessages;

import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.tcp.message.TCPServerMessage;

import java.io.Serial;
import java.rmi.RemoteException;

public class ErrorMessage implements TCPServerMessage {
    @Serial
    private static final long serialVersionUID = 170L;
    private final String excMessage;

    public ErrorMessage(String excMessage) {
        this.excMessage = excMessage;
    }

    @Override
    public boolean isCheck() {
        return true;
    }

    @Override
    public void execute(VirtualClient virtualClient) throws RemoteException {

    }
}
