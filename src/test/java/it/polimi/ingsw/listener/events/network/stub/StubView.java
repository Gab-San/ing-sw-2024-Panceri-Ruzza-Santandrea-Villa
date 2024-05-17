package it.polimi.ingsw.listener.events.network.stub;

import it.polimi.ingsw.model.Player;

import java.util.LinkedList;
import java.util.List;

public class StubView {
    private final List<Player> playerList;

    public StubView() {
        this.playerList = new LinkedList<>();
    }

    public Player getPlayer(String nickname){
        return playerList.stream().filter(p -> p.getNickname().equals(nickname)).findAny().orElseThrow();
    }

    public void addPlayer(String nickname){
        playerList.add(new Player(nickname));
    }

}
