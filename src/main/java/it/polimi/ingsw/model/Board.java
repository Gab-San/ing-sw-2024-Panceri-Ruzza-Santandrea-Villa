package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.model.deck.ObjectiveDeck;
import it.polimi.ingsw.model.deck.PlayableDeck;
import it.polimi.ingsw.model.deck.StartingCardDeck;
import it.polimi.ingsw.model.deck.cardfactory.GoldCardFactory;
import it.polimi.ingsw.model.deck.cardfactory.ResourceCardFactory;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.exceptions.DeckException;
import it.polimi.ingsw.model.exceptions.DeckInstantiationException;

import java.security.InvalidParameterException;
import java.util.*;

//TODO: Create custom Exceptions (like InvalidAction??)
public class Board {
    //TODO: add decks
    public static final int ENDGAME_SCORE = 20;
    public static final int MAX_PLAYERS = 4;
    private final Map<Player, Integer> scoreboard;
    private final Map<Player, PlayArea> playerAreas;
    private final PlayableDeck resourceDeck, goldDeck;
    private final ObjectiveDeck objectiveDeck;
    private final StartingCardDeck startingDeck;
    public static final char STARTING_DECK = 's';
    public static final char OBJECTIVE_DECK = 'o';
    public static final char RESOURCE_DECK = 'r';
    public static final char GOLD_DECK = 'g';
    private int currentTurn;
    private GamePhase gamePhase;

    private final Game gameInfo;

    protected Board(String gameID) throws DeckInstantiationException {
        currentTurn = 0;
        scoreboard = new Hashtable<>();
        playerAreas = new Hashtable<>();
        gameInfo = new Game(gameID);
        resourceDeck = new PlayableDeck(new ResourceCardFactory());
        goldDeck = new PlayableDeck(new GoldCardFactory());
        objectiveDeck = new ObjectiveDeck();
        startingDeck = new StartingCardDeck();
    }

    /**
     * Constructs the Board (as in initializing the game)
     * @param players 1-4 players that are joining this game
     * @throws InvalidParameterException if the parameter players has an illegal player count (0 or >4)
     * @throws RuntimeException if players contains duplicates
     */
    public Board(String gameID, Player ...players) throws InvalidParameterException, DeckInstantiationException {
        this(gameID);
        if(players.length < 1 || players.length > MAX_PLAYERS) throw new InvalidParameterException("Illegal number of players! Too high.");
        for(Player p : players) {
            addPlayer(p);
        }
    }

    public Board(String gameID, List<Player> players) throws InvalidParameterException, DeckInstantiationException {
        this(gameID);
        if(players.isEmpty() || players.size() > MAX_PLAYERS) throw new InvalidParameterException("Illegal number of players!");
        for(Player p: players){
            addPlayer(p);
        }
    }

    public int getCurrentTurn() {
        return currentTurn;
    }
    public void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
    }
    /**
     * Increments currentTurn, unless it's the last turn (turn == num players) in which case it sets currentTurn to 1.
     */
    public void nextTurn(){
        if(currentTurn >= playerAreas.size())
            currentTurn = 1;
        else
            currentTurn++;
    }
    public GamePhase getGamePhase() {
        return gamePhase;
    }
    public void setGamePhase(GamePhase gamePhase) {
        this.gamePhase = gamePhase;
    }
    public Game getGameInfo(){
        return gameInfo;
    }
    /**
     * @return list of players ordered by their turn
     */
    public List<Player> getPlayersByTurn(){
        return playerAreas.keySet().stream()
                .sorted(Comparator.comparingInt(Player::getTurn))
                .toList();
    }
    /**
     * @return list of players ordered by their score
     */
    public List<Player> getPlayersByScore(){
        return playerAreas.keySet().stream()
                .sorted(Comparator.comparingInt(scoreboard::get))
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
        if(player == null) throw new NullPointerException("Null player reference.");
        if(playerAreas.containsKey(player)) throw new RuntimeException("Cannot add a player already in game.");
        if(playerAreas.size() >= MAX_PLAYERS) throw new RuntimeException("Max number of players already in game.");
        setScore(player, 0);
        playerAreas.put(player, new PlayArea());
        player.setTurn(playerAreas.size());
    }
    public Map<Player, Integer> getScoreboard(){
        return Collections.unmodifiableMap(scoreboard);
    }
    public void addScore(Player player, int amount){
        int newScore = scoreboard.get(player) + amount;
        setScore(player, newScore);
    }
    public void setScore(Player player, int score){
        scoreboard.put(player, score);
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
        //TODO card face should be controller by the card not by the method
        if(placeOnFront)
            startingCard.turnFaceUp();
        else
            startingCard.turnFaceDown();

        playerAreas.get(player).placeStartingCard(startingCard); // throws RuntimeException if the startingCard was already placed
    }


    public void deal(char deck, PlayerHand playerHand) throws IllegalStateException, DeckException {
        //TODO [Gamba] choose whether to handle the deck exception
        switch (deck){
            case 's':
                playerHand.setCard(startingDeck.getCard());
                break;
            case 'o':
                playerHand.setCard(objectiveDeck.getCard());
                playerHand.setCard(objectiveDeck.getCard());
                break;
            default:
                throw new IllegalStateException("Choosing a non-dealable deck");
        }
    }

    public List<ObjectiveCard> getRevealedObjectives(){
        List<ObjectiveCard> revealedObj = new ArrayList<>();
        revealedObj.add(objectiveDeck.getFirstRevealed());
        revealedObj.add(objectiveDeck.getSecondRevealed());
        return revealedObj;
    }

    public void drawTop(char deck, PlayerHand playerHand) throws IllegalStateException, DeckException {
        switch (deck){
            case 'r':
                playerHand.addCard(resourceDeck.getTopCard());
                break;
            case 'g':
                playerHand.addCard(goldDeck.getTopCard());
                break;
            default:
                throw new IllegalStateException("Choosing a non-drawable deck");
        }
    }

    public void drawFirst(char deck, PlayerHand playerHand) throws IllegalStateException, DeckException {
        switch (deck){
            case 'r':
                playerHand.addCard(resourceDeck.getFirstRevealedCard());
                break;
            case 'g':
                playerHand.addCard(goldDeck.getFirstRevealedCard());
                break;
            default:
                throw new IllegalStateException("Choosing a non-drawable deck");
        }
    }

    public void drawSecond(char deck, PlayerHand playerHand) throws IllegalStateException, DeckException {
        switch (deck){
            case 'r':
                playerHand.addCard(resourceDeck.getSecondRevealedCard());
                break;
            case 'g':
                playerHand.addCard(goldDeck.getSecondRevealedCard());
                break;
            default:
                throw new IllegalStateException("Choosing a non-drawable deck");
        }
    }
}
