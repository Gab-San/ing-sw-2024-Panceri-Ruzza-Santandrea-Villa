package it.polimi.ingsw.controller;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.controller.timer.TurnTimerController;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.network.VirtualClient;

import java.util.List;

public abstract class GameState {
    protected final Board board;
    protected final BoardController controller;
    protected final List<String> disconnectingPlayers;
    protected final TurnTimerController timers;

    public GameState(Board board, BoardController controller, List<String> disconnectingPlayers) {
        this.board = board;
        this.controller = controller;
        this.disconnectingPlayers = disconnectingPlayers;
        this.timers = new TurnTimerController(controller);
    }

    abstract public void join (String nickname, VirtualClient client) throws IllegalStateException;
    abstract public void setNumOfPlayers(String nickname, int num) throws IllegalStateException, IllegalArgumentException;

    abstract public void disconnect (String nickname) throws IllegalStateException, IllegalArgumentException;
    abstract public void placeStartingCard(String nickname, boolean placeOnFront) throws IllegalStateException, IllegalArgumentException;
    abstract public void chooseYourColor(String nickname, PlayerColor color) throws IllegalStateException, IllegalArgumentException;

    abstract public void chooseSecretObjective(String nickname, int choice) throws IllegalStateException, IllegalArgumentException;
    abstract public void placeCard(String nickname, String cardID, Point cardPos, CornerDirection cornerDir, boolean placeOnFront) throws IllegalStateException, IllegalArgumentException;
    abstract public void draw (String nickname, char deckFrom, int cardPos) throws IllegalStateException, IllegalArgumentException;
    abstract public void restartGame (String nickname, int numOfPlayers) throws IllegalStateException, IllegalArgumentException;
    protected void transition(GameState nextState){
        timers.stopAll();
        controller.setGameState(nextState);
    }
}
