package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.server.VirtualClient;

public abstract class GameState {
    protected final Board board;

    public GameState(Board board) {
        this.board = board;
    }

    abstract public void join (String nickname, VirtualClient client) throws Exception;
    public void disconnect (String nickname, VirtualClient client) throws Exception{
        synchronized (board){
            board.removePlayer(nickname); // throws exception if player isn't in game
            board.getGameInfo().removeClient(client);
        }
    }
    abstract public GameState startGame (String nickname) throws Exception;
    abstract public void placeStartingCard(String nickname, boolean placeOnFront) throws Exception;
    abstract public GameState chooseSecretObjective(String nickname, int choice) throws Exception;
    abstract public GameState draw (String nickname, int deck, int card) throws Exception;
    abstract public void placeCard (String nickname, PlayCard card, Corner corner) throws Exception;
}
