package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Point;
import it.polimi.ingsw.model.enums.CornerDirection;
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
        giveStartingCard();




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
    public void placeStartingCard(String nickname, boolean placeOnFront) throws IllegalStateException {
        if(!board.getGamePhase().equals("GIVE STARTING CARD PHASE"))
            throw new IllegalStateException("IMPOSSIBLE TO PLACE A STARTING CARD IN THIS PHASE");
        if(playersWhoPlacedStartingCard.contains(nickname))
            throw new IllegalStateException(nickname + " has already placed their starting card.");
        Player player = board.getPlayerByNickname(nickname);
        this.board.placeStartingCard(player, placeOnFront);

        playersWhoPlacedStartingCard.add(nickname);
        if(playersWhoPlacedStartingCard.size() == board.getPlayerAreas().size()){
            board.setGamePhase(GamePhase.CPCP);
        }
    }
    public void chooseYourColor(String nickname, PlayerColor color) throws IllegalStateException,DeckException {
        if(!board.getGamePhase().equals("CHOOSE YOUR COLOR PHASE"))
            throw new IllegalStateException("IMPOSSIBLE TO CHOOSE A COLOR IN THIS PHASE");
        //it's needed to control whether all starting cards have been placed
        if(playersWhoPlacedStartingCard.size()< board.getPlayerAreas().size())
            throw new IllegalStateException("It's needed to wait for everybody to place their starting card.");

        if(playersWhoChoseColor.contains(nickname))
            throw new IllegalStateException(nickname + " has already chosen his color.");
        Player player = this.board.getPlayerByNickname(nickname);
        player.setColor(color);

        playersWhoChoseColor.add(nickname);
        if(playersWhoChoseColor.size()==board.getPlayerAreas().size()) {
            board.setGamePhase(GamePhase.DFHP);
            drawFirstHand();
            board.setGamePhase(GamePhase.DSOCP);
            giveSecreteObjectives();
            board.setGamePhase(GamePhase.CSOCP);
        }
    }

    @Override
    public GameState chooseSecretObjective(String nickname, int choice) throws IllegalStateException {
        if(!board.getGamePhase().equals("SELECT SECRETE OBJECTIVE CARD PHASE"))
            throw new IllegalStateException("IMPOSSIBLE TO CHOOSE A SECRETE OBJECTIVE CARD IN THIS PHASE");
        //it's needed to control whether all starting cards have been placed and color chosen
        if(playersWhoPlacedStartingCard.size()!= board.getPlayerAreas().size())
            throw new IllegalStateException("It's needed to wait for everybody to place their starting card.");
        if(playersWhoChoseColor.size()!= board.getPlayerAreas().size())
            throw new IllegalStateException("It's needed to wait for everybody to choose their color.");

        if(playersWhoChoseSecretObjective.contains(nickname))
            throw new IllegalStateException(nickname + " has already chosen his secrete objective card.");
        Player player = board.getPlayerByNickname(nickname);
        player.getHand().chooseObjective(choice);
        playersWhoChoseSecretObjective.add(nickname);

        if(playersWhoChoseSecretObjective.size() == board.getPlayerAreas().size()) {
            board.setGamePhase(GamePhase.CFPP);
            return nextState();
        }
        else
            return this;
    }

    @Override
    public GameState draw(String nickname, String cardToDraw) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO DRAW DURING SETUP STATE");
    }

    @Override
    public void placeCard(String nickname, String cardID, Point cardPos, CornerDirection cornerDir) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO PLACE CARDS OTHER THAN THE STARTING CARD DURING SETUP STATE");
    }

    private GameState nextState() throws IllegalStateException {
        board.setCurrentTurn(1);
        return new PlayState(this.board);
    }

    private void drawFirstHand() throws IllegalStateException, DeckException {
        for(Player player : board.getPlayerAreas().keySet()) {
            board.drawTop(Board.RESOURCE_DECK, player.getHand());
            board.drawTop(Board.RESOURCE_DECK, player.getHand());
            board.drawTop(Board.GOLD_DECK, player.getHand());
        }
    }
    private void giveSecreteObjectives(){
        for(Player player : board.getPlayerAreas().keySet()) {
            try {board.deal(Board.OBJECTIVE_DECK, player.getHand());}
            catch (DeckException e){/*TODO: handling exception */ System.err.println("giveSecreteObjectives error");}
        }
    }
    private void giveStartingCard(){
        for(Player player : board.getPlayerAreas().keySet()) {
            try {board.deal(Board.STARTING_DECK, player.getHand());}
            catch (DeckException e){/*TODO: handling exception */ System.err.println("giveStartingCard error");}
        }
    }

    @Override
    public GameState startGame (String gameID, int numOfPlayers) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO START GAME DURING SETUP STATE");
    }
}
