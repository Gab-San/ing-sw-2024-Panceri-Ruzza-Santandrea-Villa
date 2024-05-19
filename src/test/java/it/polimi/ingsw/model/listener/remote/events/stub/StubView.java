package it.polimi.ingsw.model.listener.remote.events.stub;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enums.PlayerColor;

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

    public void addPlayer(String nickname, boolean isConnected, int turn, PlayerColor colour){
        Player player = new Player(nickname);
        player.setConnected(isConnected);
        player.setTurn(turn);
        player.setColor(colour);
        playerList.add(player);

    }

}
