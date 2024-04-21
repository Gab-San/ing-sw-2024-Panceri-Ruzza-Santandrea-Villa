package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.StartingCard;

import java.security.InvalidParameterException;
import java.util.*;

//TODO: Create custom Exceptions (like InvalidAction??)
public class Board {
    //TODO: add decks
    public static final int ENDGAME_SCORE = 20;
    public static final int MAX_PLAYERS = 4;
    private final Map<Player, Integer> scoreboard;
    private final Map<Player, PlayArea> playerAreas;

    //TODO: move currentTurn to Game class?
    private int currentTurn;

    protected Board(){
        currentTurn = 0;
        scoreboard = new Hashtable<>();
        playerAreas = new Hashtable<>();
    }

    /**
     * Constructs the Board (as in initializing the game)
     * @param players all (2-4) players that are joining this game
     * @throws InvalidParameterException if the parameter players has an illegal player count
     * @throws RuntimeException if players contains duplicates
     */
    public Board(Player ...players) throws InvalidParameterException, RuntimeException {
        this();
        if(players.length > MAX_PLAYERS) throw new InvalidParameterException("Illegal number of players! Too high.");
        for(Player p : players) {
            addPlayer(p);
        }
    }

    /**
     * @return list of players ordered by their turn
     */
    public List<Player> getPlayers(){
        return playerAreas.keySet().stream()
                .sorted(Comparator.comparingInt(Player::getTurn))
                .toList();
    }
    //FIXME: is this useful??
    public Map<Player, PlayArea> getPlayerAreas(){
        return Collections.unmodifiableMap(playerAreas);
    }

    /**
     * Adds a player to the game
     * @param player player that is joining the game
     * @throws RuntimeException if the player had already joined or if the current game is full (4 players)
     */
    public void addPlayer(Player player) throws RuntimeException{
        if(playerAreas.containsKey(player)) throw new RuntimeException("Cannot add a player already in game.");
        if(playerAreas.size() >= MAX_PLAYERS) throw new RuntimeException("Max number of players already in game.");
        setScore(player, 0);
        playerAreas.put(player, new PlayArea());
        player.setTurn(playerAreas.size());
    }
    public Map<Player, Integer> getScoreboard(){
        return Collections.unmodifiableMap(scoreboard);
    }
    public void addScore(Player p, int amount){
        scoreboard.replace(
            p,
            scoreboard.get(p) + amount
        );
    }
    public void setScore(Player p, int score){
        scoreboard.put(
                p,
                score
        );
    }
    public boolean checkEndgame(){
        return scoreboard.values().stream()
                .anyMatch(score -> score >= ENDGAME_SCORE);
    }

    /**
     * Places the given card on the given corner on player's playArea
     * @param player the player doing the placement action
     * @param card the card to be placed
     * @param corner the corner on which to place card
     * @throws InvalidParameterException if player isn't in game, card isn't in player's hand or corner is
     * @throws RuntimeException if the placement is invalid (as per PlayArea.placeCard())
     */
    //TODO: review Board.placeCard() args after making client/view
    public void placeCard(Player player, PlayCard card, Corner corner) throws InvalidParameterException, RuntimeException{
        //checks
        if(!playerAreas.containsKey(player)) throw new InvalidParameterException("Player not in this game!");
        if(!player.getHand().containsCard(card)) throw new InvalidParameterException("Card not in player's hand!");

        //placement
        PlayArea playArea = playerAreas.get(player);
        PlayCard placedCard = playArea.placeCard(card, corner); // throws RuntimeException if the placement is invalid
        player.getHand().removeCard(card);

        //scoreboard update
        addScore(player, placedCard.calculatePointsOnPlace(playArea));
    }
    /**
     * Places the player's starting card on their playArea
     * @param player the player doing the placement action
     * @param placeOnFront the facing on which to place the player's starting card
     * @throws RuntimeException if the action is invalid (e.g. if the player hasn't received a startingCard yet or has already placed it)
     */
    public void placeStartingCard(Player player, boolean placeOnFront) throws RuntimeException{
        StartingCard startingCard = player.getHand().getStartingCard();
        if(startingCard == null) throw new RuntimeException("Player doesn't have a starting card yet!");
        if(placeOnFront)
            startingCard.turnFaceUp();
        else
            startingCard.turnFaceDown();

        playerAreas.get(player).placeStartingCard(startingCard); // throws RuntimeException if the startingCard was already placed
        player.getHand().setStartingCard(null); //FIXME: should we do this??
    }
    // TODO: methods to give a startingCard and a secretObjective to the player (through the decks)
    // TODO: methods to draw cards (through the decks)
}
