package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.StartingCard;

public class EndgameState extends GameState{
    public EndgameState(Board board) {
        super(board);
    }

    @Override
    public void join(String nickname, String gameID) throws Exception {

    }

    @Override
    public void disconnect(String nickname, VirtualClient client) throws Exception {

    }

    @Override
    public GameState startGame() throws Exception {

        return null;
    }

    @Override
    public void placeStartingCard(String nickname, StartingCard card, Boolean placeOnFront) throws Exception {

    }

    @Override
    public void chooseSecreteObjective(String nickname, ObjectiveCard card, Boolean placeOnFront) throws Exception {

    }

    @Override
    public GameState draw(String nickname, int deck, int card) throws Exception {

        return null;
    }

    @Override
    public void placeCard(String nickname, PlayCard card, Corner corner) throws Exception {

    }

    @Override
    public GameState nextState() throws Exception {
        //to implement, rete is needed
        //to send a message to the players to see if they want to connect to a new game or not
        return null;
    }
}
