package it.polimi.ingsw.server.Commands;

import it.polimi.ingsw.server.CentralServer;
import it.polimi.ingsw.server.VirtualClient;

public class DisconnectCmd implements ServerCommand{
    private final CentralServer serverRef;
    private final String nickname;
    private final VirtualClient client;
    public DisconnectCmd(String nickname, VirtualClient client){
        serverRef = CentralServer.getSingleton();
        this.nickname = nickname;
        this.client = client;
    }
    @Override
    public void execute() {
        serverRef.disconnect(nickname, client);
    }
}
