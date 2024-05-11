package it.polimi.ingsw.server.tcp.message.errors;

import it.polimi.ingsw.server.tcp.TCPClient;

import java.io.Serial;

public class ErrorConnectMessage extends ErrorMessage{
    @Serial
    private static final long serialVersionUID = 167L;
    private final String nickname;
    private final boolean isError;
    public ErrorConnectMessage(String nickname, boolean isError){
        super(null);
        this.nickname = nickname;
        this.isError = isError;
    }
    public ErrorConnectMessage(String nickname, boolean isError, String excMessage) {
        super(excMessage);
        this.nickname = nickname;
        this.isError = isError;
    }

    @Override
    public void handle(TCPClient client){
        if(isError){
            super.handle(client);
            return;
        }
        client.establishConnection(nickname);
    }
}
