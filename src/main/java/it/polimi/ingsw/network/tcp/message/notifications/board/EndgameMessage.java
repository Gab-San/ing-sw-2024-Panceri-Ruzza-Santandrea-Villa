package it.polimi.ingsw.network.tcp.message.notifications.board;

import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.tcp.message.TCPServerMessage;

import java.io.Serial;
import java.rmi.RemoteException;

public class EndgameMessage implements TCPServerMessage {
    @Serial
    private static final long serialVersionUID = 198429418L;
    private final String nickname;
    private final int score;
    private final boolean emptyDecks;

    public EndgameMessage(String nickname, int score) {
        this.nickname = nickname;
        this.score = score;
        this.emptyDecks = false;
    }

    public EndgameMessage(){
        this.nickname = null;
        this.score = 0;
        this.emptyDecks = true;
    }

    @Override
    public boolean isCheck() {
        return false;
    }

    @Override
    public void execute(VirtualClient virtualClient) throws RemoteException {

    }
}
