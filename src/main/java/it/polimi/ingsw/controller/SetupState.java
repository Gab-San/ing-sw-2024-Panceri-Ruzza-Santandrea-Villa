package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.PlayerColor;
import it.polimi.ingsw.model.exceptions.DeckException;
import it.polimi.ingsw.server.VirtualClient;

import java.util.HashSet;
import java.util.Set;

public class SetupState extends GameState{
    public Set<String> playersWhoPlacedStartingCard;
    public Set<String> playersWhoChoseColor;
    public Set<String> playersWhoChoseSecretObjective;
    public SetupState(Board board) {
        super(board);
        playersWhoPlacedStartingCard = new HashSet<>();
        playersWhoChoseColor=new HashSet<>();
        playersWhoChoseSecretObjective=new HashSet<>();
        board.setGamePhase(GamePhase.CBP);
        board.setGamePhase(GamePhase.GSCP);


    }

    @Override
    public GameState join(String nickname, VirtualClient client) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO JOIN A GAME DURING SETUP STATE");
    }

    @Override
    public GameState setNumOfPlayers(String nickname, int num) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO CHANGE THE NUMBER OF PLAYERS DURING SETUP STATE");
    }

    @Override
    public GameState startGame(String nickname) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO START GAME DURING SETUP STATE");
    }

    @Override
    public void placeStartingCard(String nickname, boolean placeOnFront) throws IllegalStateException {
            if(playersWhoPlacedStartingCard.contains(nickname))
                throw new IllegalStateException(nickname + " already placed their starting card.");
            Player player = board.getPlayerByNickname(nickname);
            this.board.placeStartingCard(player, placeOnFront);

            playersWhoPlacedStartingCard.add(nickname);
            if(playersWhoPlacedStartingCard.size() == board.getPlayerAreas().size()){
                board.setGamePhase(GamePhase.CPCP;

            }
    }
    public void chooseYourColor(String nickname, PlayerColor color) throws IllegalStateException,DeckException {
        //it's needed to control whether all starting cards have been placed
        if(playersWhoPlacedStartingCard.size()!= board.getPlayerAreas().size())
            throw new IllegalStateException("It's needed to wait for everybody to place their starting card.");

        if(playersWhoChoseColor.contains(nickname))
            throw new IllegalStateException(nickname + " already chose his color.");
        Player player = this.board.getPlayerByNickname(nickname);
        player.setColor(color);

        playersWhoChoseColor.add(nickname);
        if(playersWhoChoseColor.size()==board.getPlayerAreas().size()) {
            drawFirstHand();
            giveToChooseObjectives();
            //TODO: ask players to choose their secret objective

        }
    }

    @Override
    public GameState chooseSecretObjective(String nickname, int choice) throws IllegalStateException {
        //it's needed to control whether all starting cards have been placed and color chosen
        if(playersWhoPlacedStartingCard.size()!= board.getPlayerAreas().size())
            throw new IllegalStateException("It's needed to wait for everybody to place their starting card.");
        if(playersWhoChoseColor.size()!= board.getPlayerAreas().size())
            throw new IllegalStateException("It's needed to wait for everybody to choose their color.");

        Player player = board.getPlayerByNickname(nickname);
        player.getHand().chooseObjective(choice);
        playersWhoChoseSecretObjective.add(nickname);

        if(playersWhoChoseSecretObjective.size() == board.getPlayerAreas().size())
            return nextState();
        else
            return this;
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
    private void setupDecks(){

    }
    private void drawFirstHand() throws IllegalStateException, DeckException {
        for(Player player : board.getPlayerAreas().keySet()) {
            board.drawTop(Board.RESOURCE_DECK, player.getHand());
            board.drawTop(Board.RESOURCE_DECK, player.getHand());
            board.drawTop(Board.GOLD_DECK, player.getHand());
        }
    }
    private void giveToChooseObjectives(){
        for(Player player : board.getPlayerAreas().keySet()) {
            //TODO: give player the 2 secrete objectives
        }
    }
}
