package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.Point;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.PlayerColor;
import it.polimi.ingsw.model.exceptions.DeckException;
import it.polimi.ingsw.server.VirtualClient;

public abstract class GameState {
    protected final Board board;
    protected final BoardController controller;
    public GameState(Board board, BoardController controller) {
        this.board = board;
        this.controller = controller;
    }

    abstract public void join (String nickname, VirtualClient client) throws IllegalStateException;
    abstract public void setNumOfPlayers(String nickname, int num) throws IllegalStateException, IllegalArgumentException;
    abstract public void disconnect (String nickname, VirtualClient client) throws IllegalStateException, IllegalArgumentException;
    abstract public void placeStartingCard(String nickname, boolean placeOnFront) throws IllegalStateException, IllegalArgumentException;
    abstract public void chooseYourColor(String nickname, PlayerColor color) throws IllegalStateException, IllegalArgumentException, InterruptedException, DeckException;
    abstract public void chooseSecretObjective(String nickname, int choice) throws IllegalStateException, IllegalArgumentException;
    abstract public void placeCard(String nickname, String cardID, Point cardPos, CornerDirection cornerDir, boolean placeOnFront) throws IllegalStateException, IllegalArgumentException;
    abstract public void draw (String nickname, char deckFrom, int cardPos) throws IllegalStateException, IllegalArgumentException;
    abstract public void startGame (String nickname, int numOfPlayers) throws IllegalStateException, IllegalArgumentException;
    protected void transition(GameState nextState){
        if(controller == null) return;
        controller.setGameState(nextState);
    }
}
