package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.enums.PlayerColor;
import it.polimi.ingsw.server.VirtualClient;

public abstract class GameState {
    protected final Board board;

    public GameState(Board board) {
        this.board = board;
    }

    abstract public GameState join (String nickname, VirtualClient client) throws IllegalStateException;
    abstract public GameState setNumOfPlayers(String nickname, int num) throws IllegalStateException;
    public void disconnect (String nickname, VirtualClient client) throws IllegalStateException{
            board.removePlayer(nickname); // throws exception if player isn't in game
            board.getGameInfo().removeClient(client);
    }
    abstract public void placeStartingCard(String nickname, boolean placeOnFront) throws IllegalStateException;
    abstract public void chooseYourColor(String nickname, PlayerColor color) throws IllegalStateException;
    abstract public GameState chooseSecretObjective(String nickname, int choice) throws IllegalStateException;
    abstract public GameState draw (String nickname, int deck, int card) throws IllegalStateException;
    abstract public void placeCard (String nickname, PlayCard card, Corner corner) throws IllegalStateException;
    abstract public GameState startGame (String nickname) throws IllegalStateException;
}
