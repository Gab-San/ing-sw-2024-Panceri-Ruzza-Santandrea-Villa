package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.StartingCard;
import java.util.ArrayList;
import java.util.List;



public class SetupState extends GameState{
    public boolean areStartingCardsPlaced;
    public SetupState(Board board) {
        super(board);
        areStartingCardsPlaced=false;
    }

    @Override
    public void join(String nickname, String gameID) throws Exception {
        if(gameID.equals(board.getGameInfo().getGameID()))
            throw new Exception("IMPOSSIBLE TO JOIN A GAME DURING SETUP STATE");
        else throw new Exception("IMPOSSIBLE TO PLACE  CARDS DURING SETUP STATE");
    }

    @Override
    public void disconnect(String nickname, VirtualClient client) throws Exception {

    }

    @Override
    public GameState startGame() throws Exception {
        throw new Exception("IMPOSSIBLE TO PLACE  CARDS DURING SETUP STATE");
    }

    @Override
    public void placeStartingCard(String nickname, StartingCard card, Boolean placeOnFront) throws Exception {
        List<Player> players = new ArrayList<>(this.board.getPlayersByTurn());
        for(var player : players){
            if(player.getNickname().equals(nickname)) {
                //TODO: player.addInHand(card); or some other way to place the starting card
                this.board.placeStartingCard(player, placeOnFront);
            }
        }
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
        board.setCurrentTurn(0);
        return new PlayState(this.board);
    }

}
