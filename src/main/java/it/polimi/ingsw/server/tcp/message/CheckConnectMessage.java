package it.polimi.ingsw.server.tcp.message;

import it.polimi.ingsw.server.tcp.TCPClient;

public class CheckConnectMessage implements TCPServerErrorMessage {
    private final String nickname;
    private final boolean isError;
    private final String excMessage;
    public CheckConnectMessage(String nickname, boolean isError){
        this.nickname = nickname;
        this.isError = isError;
        this.excMessage = null;
    }
    public CheckConnectMessage(String nickname, boolean isError, String excMessage) {
        this.nickname = nickname;
        this.isError = isError;
        this.excMessage = excMessage;
    }

    @Override
    public boolean isError() {
        return true;
    }

    @Override
    public void handle(TCPClient client){
        if(isError){
            client.updateError(excMessage);
        }
        client.setNickname(nickname);
    }
}
