package it.polimi.ingsw.model.listener.remote.events.board;

import it.polimi.ingsw.model.listener.remote.NetworkEvent;
import it.polimi.ingsw.model.listener.remote.events.UpdateEvent;
import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

public class EndgameCheckEvent implements UpdateEvent {
    private final String nickname;
    private final int score;
    private final boolean emptyDecks;

    public EndgameCheckEvent(String nickname, int score) {
        this.nickname = nickname;
        this.score = score;
        this.emptyDecks = false;
    }

    public EndgameCheckEvent(){
        this.nickname = null;
        this.score = 0;
        this.emptyDecks = true;
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        if(emptyDecks){
            virtualClient.notifyEndgame();
            return;
        }
        virtualClient.notifyEndgame(nickname, score);
    }

    @Override
    public String toString() {
        return "EndgameCheckEvent{" +
                "nickname='" + nickname + '\'' +
                ", score=" + score +
                ", emptyDecks=" + emptyDecks +
                '}';
    }
}
