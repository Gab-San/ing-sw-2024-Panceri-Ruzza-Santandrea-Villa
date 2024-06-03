package it.polimi.ingsw.model.listener.remote.events.board;

import it.polimi.ingsw.model.listener.remote.NetworkEvent;
import it.polimi.ingsw.model.listener.remote.events.UpdateEvent;
import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

public class ChangeScoreEvent implements UpdateEvent {
    private final String nickname;
    private final int score;
    public ChangeScoreEvent(String nickname, int score) {
        this.nickname = nickname;
        this.score = score;
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.updateScore(nickname, score);
    }
}
