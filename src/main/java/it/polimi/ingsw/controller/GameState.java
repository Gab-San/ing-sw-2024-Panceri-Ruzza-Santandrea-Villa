package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.StartingCard;

//TODO: delete this after creating the actual VirtualClient
class VirtualClient{}

abstract class GameState {
    protected final Board board;


    public GameState(Board board) {
        this.board = board;
    }

    abstract public void join (String nickname, String gameID) throws Exception;
    abstract public void disconnect (String nickname, VirtualClient client) throws Exception;
    abstract public GameState startGame () throws Exception;
    abstract public void placeStartingCard(String nickname, StartingCard card, Boolean placeOnFront) throws Exception;
    abstract public void chooseSecreteObjective (String nickname, ObjectiveCard card, Boolean placeOnFront) throws Exception;
    abstract public GameState draw (String nickname, int deck, int card) throws Exception;
    abstract public void placeCard (String nickname, PlayCard card, Corner corner) throws Exception;
    abstract public GameState nextState () throws Exception;
}
