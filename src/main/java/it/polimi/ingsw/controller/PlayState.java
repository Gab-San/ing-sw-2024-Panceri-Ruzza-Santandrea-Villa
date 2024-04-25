package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.server.VirtualClient;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

public class PlayState extends GameState {
    private Set<String> playersWhoPlacedCard;
    private boolean lastTurn;

    public PlayState(Board board) {
        super(board);
        playersWhoPlacedCard = new HashSet<>();
        lastTurn=false;
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
        if(playersWhoPlacedCard.contains(nickname)) throw new Exception("Players had already placed!");

        // TODO: place the starting card

        playersWhoPlacedCard.add(nickname);
    }

    @Override
    public void chooseSecretObjective(String nickname, ObjectiveCard card, Boolean placeOnFront) throws Exception {

    }

    @Override
    public GameState draw(String nickname, int deck, int card) throws Exception {
        //TODO: this.board.draw(nickname, deck, card);
        if(lastTurn && board.getCurrentTurn()==board.getPlayerAreas().size()-1)
            return nextState();
        if(board.checkEndgame() && board.getCurrentTurn()==board.getPlayerAreas().size()-1)
            lastTurn = true;
        if(board.getCurrentTurn() != board.getPlayerAreas().keySet().size()-1)
            this.board.setCurrentTurn(board.getCurrentTurn()+1);
        else this.board.setCurrentTurn(0);
        return this;
    }

    @Override
    public void placeCard(String nickname, PlayCard card, Corner corner) throws Exception {

    }

    private GameState nextState() throws Exception {
        return new EndgameState(board);
    }
}
