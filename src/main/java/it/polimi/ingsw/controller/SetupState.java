package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.server.VirtualClient;

import java.util.HashSet;
import java.util.Set;

public class SetupState extends GameState{
    public Set<String> playersWhoPlacedStartingCard;
    public Set<String> playersWhoChoseSecretObjective;
    public SetupState(Board board) {
        super(board);
        playersWhoPlacedStartingCard = new HashSet<>();
    }

    @Override
    public GameState join(String nickname, VirtualClient client) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO JOIN A GAME DURING SETUP STATE");
    }

    @Override
    public GameState setNumOfPlayers(String nickname, int num) throws IllegalStateException {
        return null;
    }

    @Override
    public GameState startGame(String nickname) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO START GAME DURING SETUP STATE");
    }

    @Override
    public void placeStartingCard(String nickname, boolean placeOnFront) throws IllegalStateException {
        synchronized (board){
            if(playersWhoPlacedStartingCard.contains(nickname))
                throw new IllegalStateException(nickname + " already placed their starting card.");
            Player player = board.getPlayerByNickname(nickname);
            this.board.placeStartingCard(player, placeOnFront);

            playersWhoPlacedStartingCard.add(nickname);
            if(playersWhoPlacedStartingCard.size() == board.getPlayerAreas().size()){
                //TODO: implement players choosing their color
                //TODO: deal secret objectives to the players
            }
        }
    }

    @Override
    public GameState chooseSecretObjective(String nickname, int choice) throws IllegalStateException {
        synchronized (board){
            Player player = board.getPlayerByNickname(nickname);
            player.getHand().chooseObjective(choice);
            playersWhoChoseSecretObjective.add(nickname);
            if(playersWhoChoseSecretObjective.size() == board.getPlayerAreas().size())
                return nextState();
            else
                return this;
        }
    }

    @Override
    public GameState draw(String nickname, int deck, int card) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO DRAW DURING SETUP STATE");
    }

    @Override
    public void placeCard(String nickname, PlayCard card, Corner corner) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO PLACE CARDS OTHER THAN THE STARTING CARD DURING SETUP STATE");
    }

    private GameState nextState() throws IllegalStateException {
        board.setCurrentTurn(1);
        return new PlayState(this.board);
    }

}
