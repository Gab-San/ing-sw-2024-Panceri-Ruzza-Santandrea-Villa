package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Point;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.PlayerColor;
import it.polimi.ingsw.model.exceptions.DeckException;
import it.polimi.ingsw.server.VirtualClient;

public abstract class GameState {
    protected final Board board;

    public GameState(Board board) {
        this.board = board;
    }

    abstract public GameState join (String nickname, VirtualClient client) throws IllegalStateException;
    abstract public GameState setNumOfPlayers(String nickname, int num) throws IllegalStateException, IllegalArgumentException;
    public void disconnect (String nickname, VirtualClient client) throws IllegalStateException, IllegalArgumentException{
            board.removePlayer(nickname); // throws exception if player isn't in game
            board.getGameInfo().removeClient(client);
    }
    abstract public void placeStartingCard(String nickname, boolean placeOnFront) throws IllegalStateException, IllegalArgumentException;
    abstract public void chooseYourColor(String nickname, PlayerColor color) throws IllegalStateException, IllegalArgumentException, InterruptedException, DeckException;
    abstract public GameState chooseSecretObjective(String nickname, int choice) throws IllegalStateException, IllegalArgumentException;
    abstract public GameState placeCard(String nickname, String cardID, Point cardPos, CornerDirection cornerDir, boolean placeOnFront) throws IllegalStateException, IllegalArgumentException;
    abstract public GameState draw (String nickname, char deckFrom, int cardPos) throws IllegalStateException, IllegalArgumentException;
    abstract public GameState startGame (String nickname, int numOfPlayers) throws IllegalStateException, IllegalArgumentException;
}
