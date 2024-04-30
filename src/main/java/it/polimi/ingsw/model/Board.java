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

//TODO: Create custom Exceptions (like InvalidPlacement / InvalidAction??)
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
    private final Map<Player, Boolean> isPlayerDeadlocked;

    private int currentTurn;
    private GamePhase gamePhase;

    private final Game gameInfo;

    protected Board(String gameID) throws DeckInstantiationException {
        currentTurn = 1;
        scoreboard = new Hashtable<>();
        playerAreas = new Hashtable<>();
        gameInfo = new Game(gameID);
        resourceDeck = new PlayableDeck(new ResourceCardFactory());
        goldDeck = new PlayableDeck(new GoldCardFactory());
        objectiveDeck = new ObjectiveDeck();
        startingDeck = new StartingCardDeck();
        isPlayerDeadlocked = new Hashtable<>();
    }

    /**
     * Constructs the Board (as in initializing the game)
     * @param players 1-4 players that are joining this game
     * @throws InvalidParameterException if the parameter players has an illegal player count (0 or >4)
     * @throws IllegalStateException if players contains duplicates
     */
    public Board(String gameID, Player ...players) throws InvalidParameterException, IllegalStateException, DeckInstantiationException {
        this(gameID);
        if(players.length < 1 || players.length > MAX_PLAYERS) throw new InvalidParameterException("Illegal number of players! Too high.");
        for(Player p : players) {
            addPlayer(p); // never throws NullPointerException as p != null
        }
    }

    public Board(String gameID, List<Player> players) throws InvalidParameterException, IllegalStateException, DeckInstantiationException {
        this(gameID);
        if(players.isEmpty() || players.size() > MAX_PLAYERS) throw new InvalidParameterException("Illegal number of players!");
        for(Player p: players){
            addPlayer(p);
        }
    }

    //TODO: timer to check for players who lose connection during their turn
    //  we could also periodically ping the clients saved in gameInfo
    public int getCurrentTurn() {
        return currentTurn;
    }
    public void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
    }
    /**
     * Increments currentTurn, unless it's the last turn (turn == num players) in which case it sets currentTurn to 1. <br>
     * If a player can't place any cards for lack of free corners, skips that player's turn.
     * @return - true if the game can continue in the next turn
     *          <br>- false if the game can't continue because all players are deadlocked
     */
    public boolean nextTurn(){
        if(currentTurn >= playerAreas.size())
            currentTurn = 1;
        else
            currentTurn++;

        Player nextPlayer = getPlayersByTurn().get(currentTurn);
        if(playerAreas.get(nextPlayer).getFreeCorners().isEmpty()){
            //TODO: notify player of deadlock (? may not be necessary as the map 'isPlayerDeadlocked' already displays it)
            isPlayerDeadlocked.put(nextPlayer, true);
            if(isPlayerDeadlocked.containsValue(false)){ // at least one player is not deadlocked
                return nextTurn(); // skip deadlocked player's turn
            }
            else{
                return false;
            }
        }
        else return true;
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
    public Map<Player, PlayArea> getPlayerAreas(){
        return Collections.unmodifiableMap(playerAreas);
    }
    public Map<Player, Boolean> getPlayerDeadlocks(){
        return Collections.unmodifiableMap(isPlayerDeadlocked);
    }

    /**
     * Adds a player to the game
     * @param player player that is joining the game
     * @throws IllegalStateException if the player had already joined or if the current game is full (4 players)
     */
    public void addPlayer(Player player) throws NullPointerException, IllegalStateException{
        if(player == null) throw new NullPointerException("Null player reference.");
        if(playerAreas.containsKey(player)) throw new IllegalStateException("Cannot add a player already in game.");
        if(playerAreas.size() >= MAX_PLAYERS) throw new IllegalStateException("Max number of players already in game.");
        setScore(player, 0);
        playerAreas.put(player, new PlayArea());
        player.setTurn(playerAreas.size());
        isPlayerDeadlocked.put(player, false);
    }
    public Map<Player, Integer> getScoreboard(){
        return Collections.unmodifiableMap(scoreboard);
    }
    public void addScore(Player player, int amount) throws IllegalArgumentException{
        if(!scoreboard.containsKey(player)) throw new IllegalArgumentException("Player not in game!");
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
     * @throws IllegalArgumentException if player isn't in game, card isn't in player's hand or corner is
     * @throws IllegalStateException if the placement is invalid (as per PlayArea.placeCard())
     */
    //TODO: review Board.placeCard() args after making client/view
    public void placeCard(Player player, PlayCard card, Corner corner) throws IllegalArgumentException, IllegalStateException{
        //checks
        if(!playerAreas.containsKey(player)) throw new IllegalArgumentException("Player not in this game!");
        if(!player.getHand().containsCard(card)) throw new IllegalArgumentException("Card not in player's hand!");

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
     * @throws IllegalStateException if the action is invalid (e.g. if the player hasn't received a startingCard yet or has already placed it)
     */
    public void placeStartingCard(Player player, boolean placeOnFront) throws IllegalStateException{
        StartingCard startingCard = player.getHand().getStartingCard();
        //TODO card face should be controller by the card not by the method
        if(startingCard == null) throw new IllegalStateException("Player doesn't have a starting card yet!");
        if(placeOnFront)
            startingCard.turnFaceUp();
        else
            startingCard.turnFaceDown();

        playerAreas.get(player).placeStartingCard(startingCard); // throws RuntimeException if the startingCard was already placed
    }

    public void deal(char deck, PlayerHand playerHand) throws IllegalStateException, DeckException {
        //TODO [Gamba] choose whether to handle the deck exception
        switch (deck){
            case STARTING_DECK:
                playerHand.setCard(startingDeck.getCard());
                break;
            case OBJECTIVE_DECK:
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
            case RESOURCE_DECK:
                playerHand.addCard(resourceDeck.getTopCard());
                break;
            case GOLD_DECK:
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
    public boolean containsPlayer(String nickname) {
        return playerAreas.keySet().stream()
                .anyMatch(p -> p.getNickname().equals(nickname));
    }

    /**
     * @param nickname player's nickname
     * @return the Player instance of the player with given nickname
     * @throws IllegalStateException if there is no player in this game with the given nickname
     */
    public Player getPlayerByNickname(String nickname) throws IllegalStateException{
        return playerAreas.keySet().stream()
                .filter(p -> p.getNickname().equals(nickname))
                .findFirst().orElseThrow(()-> new IllegalStateException("Cannot find a player with given nickname in this game") );
    }

    public void removePlayer(String nickname) throws IllegalStateException {
        Player player = getPlayerByNickname(nickname);

        // remove playArea and scoreboard
        playerAreas.remove(player);
        scoreboard.remove(player);
        //decrement turn of each player that followed the removed player in turn order
        playerAreas.keySet().stream()
                .filter(p -> p.getTurn() > player.getTurn())
                .forEach(p -> p.setTurn(p.getTurn()-1));
        // fix current turn if it was another player's turn
        if(currentTurn >= player.getTurn())
            currentTurn--;
    }
}
