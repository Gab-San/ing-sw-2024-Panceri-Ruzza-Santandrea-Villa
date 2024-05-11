package it.polimi.ingsw.server.tcp.message.errors;

import it.polimi.ingsw.server.tcp.TCPClient;
import it.polimi.ingsw.server.tcp.message.TCPServerErrorMessage;

import java.io.Serial;

public class ErrorMessage implements TCPServerErrorMessage {
    @Serial
    private static final long serialVersionUID = 170L;
    private final String excMessage;

    public ErrorMessage(String excMessage) {
        this.excMessage = excMessage;
    }


    @Override
    public boolean isError() {
        return true;
    }

    @Override
    public void handle(TCPClient client) {
        client.updateError(excMessage);
    }
}
