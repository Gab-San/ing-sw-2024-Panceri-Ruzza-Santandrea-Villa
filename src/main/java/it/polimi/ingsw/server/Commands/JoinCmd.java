package it.polimi.ingsw.server.Commands;

import it.polimi.ingsw.server.CentralServer;
import it.polimi.ingsw.server.VirtualClient;

public class JoinCmd implements ServerCommand{
    private final CentralServer serverRef;
    private final String nickname;
    private final String gameID;
    private final VirtualClient client;
    public JoinCmd(String nickname, String gameID, VirtualClient client){
        serverRef = CentralServer.getSingleton();
        this.nickname = nickname;
        this.client = client;
        this.gameID = gameID;
    }
    @Override
    public void execute() {
        serverRef.join(nickname, gameID, client);
    }
}
