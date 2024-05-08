package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Point;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.PlayerColor;
import it.polimi.ingsw.model.exceptions.DeckException;
import it.polimi.ingsw.server.VirtualClient;

import java.util.*;

public class SetupState extends GameState{
    public Set<String> playersWhoPlacedStartingCard;
    public Set<String> playersWhoChoseColor;
    public Set<PlayerColor> playerColors;
    public Set<String> playersWhoChoseSecretObjective;
    public SetupState(Board board) {
        super(board);
        playersWhoPlacedStartingCard = new HashSet<>();
        playersWhoChoseColor=new HashSet<>();
        playerColors=new HashSet<>();
        playersWhoChoseSecretObjective=new HashSet<>();
        board.setGamePhase(GamePhase.CBP);
        board.setGamePhase(GamePhase.GSCP);
        giveStartingCard();
        board.setGamePhase(GamePhase.PSCP);
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
    public void placeStartingCard(String nickname, boolean placeOnFront) throws IllegalStateException, IllegalArgumentException {
        if(board.getGamePhase() != GamePhase.PSCP)
            throw new IllegalStateException("IMPOSSIBLE TO PLACE A STARTING CARD IN THIS PHASE");
        if(playersWhoPlacedStartingCard.contains(nickname))
            throw new IllegalStateException(nickname + " has already placed their starting card.");
        Player player = board.getPlayerByNickname(nickname);
        board.placeStartingCard(player, placeOnFront);

        playersWhoPlacedStartingCard.add(nickname);
        if(playersWhoPlacedStartingCard.size() == board.getPlayerAreas().size()){
            board.setGamePhase(GamePhase.CPCP);
        }
    }
    @Override
    public void chooseYourColor(String nickname, PlayerColor color) throws IllegalStateException, IllegalArgumentException, InterruptedException, DeckException {
        if(board.getGamePhase() != GamePhase.CPCP)
            throw new IllegalStateException("IMPOSSIBLE TO CHOOSE A COLOR IN THIS PHASE");
        //it's needed to control whether all starting cards have been placed
        if(playersWhoPlacedStartingCard.size() < board.getPlayerAreas().size())
            throw new IllegalStateException("It's needed to wait for everybody to place their starting card.");
        if(playersWhoChoseColor.contains(nickname))
            throw new IllegalStateException(nickname + " has already chosen his color.");
        if(playerColors.contains(color))
            throw new IllegalStateException(color + " not available.");

        Player player = this.board.getPlayerByNickname(nickname);
        player.setColor(color);

        playerColors.add(color);
        playersWhoChoseColor.add(nickname);
        if(playersWhoChoseColor.size()==board.getPlayerAreas().size()) {
            board.setGamePhase(GamePhase.DFHP);
            drawFirstHand();
            board.setGamePhase(GamePhase.DSOCP);
            giveSecretObjectives();
            board.setGamePhase(GamePhase.CSOCP);
        }
    }

    @Override
    public GameState chooseSecretObjective(String nickname, int choice) throws IllegalStateException, IllegalArgumentException {
        if(board.getGamePhase() != GamePhase.CSOCP)
            throw new IllegalStateException("IMPOSSIBLE TO CHOOSE A SECRET OBJECTIVE CARD IN THIS PHASE");
        //it's needed to control whether all starting cards have been placed and color chosen
        if(playersWhoPlacedStartingCard.size() != board.getPlayerAreas().size())
            throw new IllegalStateException("It's needed to wait for everybody to place their starting card.");
        if(playersWhoChoseColor.size() != board.getPlayerAreas().size())
            throw new IllegalStateException("It's needed to wait for everybody to choose their color.");
        if(playersWhoChoseSecretObjective.contains(nickname))
            throw new IllegalStateException(nickname + " has already chosen his secret objective card.");

        Player player = board.getPlayerByNickname(nickname); // throws IllegalArgumentException on player not in game
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
    public GameState draw(String nickname, char deckFrom, int cardPos) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO DRAW DURING SETUP STATE");
    }

    @Override
    public GameState placeCard(String nickname, String cardID, Point cardPos, CornerDirection cornerDir, boolean placeOnFront) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO PLACE CARDS OTHER THAN THE STARTING CARD DURING SETUP STATE");
    }

    private GameState nextState() throws IllegalStateException {
        board.setCurrentTurn(1);
        List<Player> players = new LinkedList<>(board.getPlayerAreas().keySet());
        final int numOfPlayers = players.size();
        Random random = new Random();
        for (int i = 1; i < numOfPlayers; i++) {
            int randomIndex = random.nextInt(players.size());
            players.get(randomIndex).setTurn(i);
            players.remove(randomIndex);
        }
        return new PlayState(board);
    }

    private void drawFirstHand() throws IllegalStateException, DeckException, InterruptedException {
        int drawSleepTime = 3;
        for(Player player : board.getPlayerAreas().keySet()) {
            board.drawTop(Board.RESOURCE_DECK, player.getHand());
            Thread.sleep(drawSleepTime);
            board.drawTop(Board.RESOURCE_DECK, player.getHand());
            Thread.sleep(drawSleepTime);
            board.drawTop(Board.GOLD_DECK, player.getHand());
            Thread.sleep(drawSleepTime);
        }
    }
    private void giveSecretObjectives(){
        for(Player player : board.getPlayerAreas().keySet()) {
            try {board.deal(Board.OBJECTIVE_DECK, player.getHand());}
            catch (DeckException e){/*TODO: handling exception */ System.err.println("giveSecretObjectives error");}
        }
    }
    private void giveStartingCard(){
        for(Player player : board.getPlayerAreas().keySet()) {
            try {board.deal(Board.STARTING_DECK, player.getHand());}
            catch (DeckException e){/*TODO: handling exception */ System.err.println("giveStartingCard error");}
        }
    }

    @Override
    public GameState startGame (String nickname, int numOfPlayers) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO START GAME DURING SETUP STATE");
    }
}
