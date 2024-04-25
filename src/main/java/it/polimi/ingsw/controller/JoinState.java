package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.model.enums.PlayerColor;
import it.polimi.ingsw.server.VirtualClient;

public class JoinState extends GameState {

    public JoinState(Board board){
        super(board);
    }

    @Override
    public void join(String nickname, String gameID) throws Exception {

        //TODO: it would be convenient to have an automatic constructor for players
        try{
            this.board.addPlayer(new Player(nickname, PlayerColor.BLUE, 0));
        }
        catch (Exception e) {System.err.println("IMPOSSIBLE TO ADD THE PLAYER");}
    }

    @Override
    public void disconnect(String nickname, VirtualClient client) throws Exception {

    }

    @Override
    public  GameState startGame() throws Exception {
        int responses=0;
        //send a message to all players in thread
        //wait for the responses
        if(responses==4)
            return this.nextState();
        return null;
    }

    @Override
    public void placeStartingCard(String nickname, StartingCard card, Boolean placeOnFront) throws Exception {
        throw new Exception("IMPOSSIBLE TO PLACE STARTING CARDS DURING JOIN STATE");
    }

    @Override
    public void chooseSecretObjective(String nickname, ObjectiveCard card, Boolean placeOnFront) throws Exception {
        throw new Exception("IMPOSSIBLE TO CHOOSE SECRETE OBJECTIVE DURING JOIN STATE");
    }

    @Override
    public GameState draw(String nickname, int deck, int card) throws Exception {
        throw new Exception("IMPOSSIBLE TO DRAW STARTING CARDS DURING JOIN STATE");
    }

    @Override
    public void placeCard(String nickname, PlayCard card, Corner corner) throws Exception {
        throw new Exception("IMPOSSIBLE TO PLACE CARDS DURING JOIN STATE");
    }

    private GameState nextState() throws Exception {
        return new SetupState(board);
    }
}
