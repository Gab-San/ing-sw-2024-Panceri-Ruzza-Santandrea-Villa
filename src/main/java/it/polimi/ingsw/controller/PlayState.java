package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.StartingCard;


public class PlayState extends GameState {
    private boolean hasPlacedCard;
    private  boolean lastTurn;

    public PlayState(Board board) {
        super(board);
        hasPlacedCard = false;
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

    }

    @Override
    public void chooseSecreteObjective(String nickname, ObjectiveCard card, Boolean placeOnFront) throws Exception {

    }

    @Override
    public GameState draw(String nickname, int deck, int card) throws Exception {
        //TODO: this.board.draw(nickname, deck, card);
        if(lastTurn && board.getCurrentTurn()==board.getPlayersByTurn().size()-1)
            return this.nextState();
        if(board.checkEndgame() && board.getCurrentTurn()==board.getPlayerAreas().keySet().size()-1)
            lastTurn = true;
        if(board.getCurrentTurn()!=board.getPlayerAreas().keySet().size()-1)
            this.board.setCurrentTurn(this.board.getCurrentTurn()+1);
        else this.board.setCurrentTurn(0);
        return this;
    }

    @Override
    public void placeCard(String nickname, PlayCard card, Corner corner) throws Exception {

    }

    @Override
    public GameState nextState() throws Exception {
        return new EndgameState(this.board);
    }
}
