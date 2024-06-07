package it.polimi.ingsw.stub;

import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.SerializableCorner;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class StubView {
    private final List<Player> playerList;
    private GamePhase gamePhase;
    private int turn;
    private final Map<String, Integer> scoreboard;
    private boolean endgame;
    private List<SerializableCorner> freeCorners;
    private Map<GameResource, Integer> visibleResources;

    public StubView() {
        this.playerList = new LinkedList<>();
        this.scoreboard = new HashMap<>();
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

    public void removePlayer(String nickname) {
        playerList.remove(getPlayer(nickname));
    }

    public void setPhase(GamePhase gamePhase) {
        this.gamePhase = gamePhase;
    }

    public GamePhase getGamePhase() {
        return gamePhase;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public Map<String, Integer> getScoreboard() {
        return scoreboard;
    }

    public void setScoreboard(String nick, int score) {
        this.scoreboard.put(nick, score);
    }

    public boolean isEndgame() {
        return endgame;
    }

    public void setEndgame(boolean endgame) {
        this.endgame = endgame;
    }

    public List<SerializableCorner> getFreeCorners() {
        return freeCorners;
    }

    public void setFreeCorners(List<SerializableCorner> freeCorners) {
        this.freeCorners = freeCorners;
    }

    public Map<GameResource, Integer> getVisibleResources() {
        return visibleResources;
    }

    public void setVisibleResources(Map<GameResource, Integer> visibleResources) {
        this.visibleResources = visibleResources;
    }
}
