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
import it.polimi.ingsw.model.exceptions.PlayerHandException;

import java.security.InvalidParameterException;
import java.util.*;

public class Board {
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

    /**
     * Constructs the Board (as in initializing the game)
     * @throws DeckInstantiationException if the decks can't be initialized
     */
    public Board(String gameID) throws DeckInstantiationException {
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
     * Constructs the board (as in initializing the game) and automatically makes the given players join
     * @param players 1-4 players that are joining this game
     * @throws InvalidParameterException if the parameter players has an illegal player count (0 or >4)
     * @throws IllegalStateException if players contains duplicates
     * @throws DeckInstantiationException if the decks can't be initialized
     */
    public Board(String gameID, Player ...players) throws InvalidParameterException, IllegalStateException, DeckInstantiationException {
        this(gameID);
        if(players.length < 1 || players.length > MAX_PLAYERS) throw new InvalidParameterException("Illegal number of players! Too high.");
        for(Player p : players) {
            addPlayer(p); // never throws NullPointerException as p != null
        }
    }

    /**
     * Constructs the board (as in initializing the game) and automatically makes the given players join
     * @param gameID game's ID
     * @param players list of 1-4 players that are joining this game
     * @throws InvalidParameterException if the list of players has an illegal player count (0 or >4)
     * @throws IllegalStateException if players contains duplicates
     * @throws DeckInstantiationException if the decks can't be initialized
     */
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
     * If a player is disconnected, skips that player's turn. <br>
     * If a player can't place any cards for lack of free corners, skips that player's turn.
     * @return - true if the game can continue in the next turn <br>
     *         - false if the game can't continue because all players are deadlocked or disconnected
     */
    public boolean nextTurn(){
        int playersWhoCanPlay = playerAreas.size();
        for(Player p : playerAreas.keySet()){
            if(playerAreas.get(p).getFreeCorners().isEmpty() && !playerAreas.get(p).getCardMatrix().isEmpty())
                isPlayerDeadlocked.put(p, true); //deadlocked == no free corners but at least one card placed (starting)
            if(!p.isConnected() || isPlayerDeadlocked.get(p))
                playersWhoCanPlay--;
        }
        if(playersWhoCanPlay == 0) return false;

        Player nextPlayer;
        List<Player> playersByTurn = getPlayersByTurn();
        do {
            if(currentTurn >= playerAreas.size())
                currentTurn = 1;
            else
                currentTurn++;
            nextPlayer = playersByTurn.get(currentTurn-1);
        }while(!nextPlayer.isConnected() || isPlayerDeadlocked.get(nextPlayer));

        return true;
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
        return playerAreas;
    }
    public Map<Player, Boolean> getPlayerDeadlocks(){
        return isPlayerDeadlocked;
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
        return scoreboard;
    }

    /**
     * Adds points to player's score
     * @param player player to whom the points will be awarded
     * @param amount number of points to award
     * @throws IllegalArgumentException if the player isn't in game
     */
    public void addScore(Player player, int amount) throws IllegalArgumentException{
        if(!scoreboard.containsKey(player)) throw new IllegalArgumentException("Player not in game!");
        int newScore = scoreboard.get(player) + amount;
        setScore(player, newScore);
    }
    protected void setScore(Player player, int score){
        scoreboard.put(player, score);
    }

    /**
     * @return true if any player has a score >= ENDGAME_SCORE or if both decks are empty <br>
     */
    public boolean checkEndgame(){
        return scoreboard.values().stream()
                .anyMatch(score -> score >= ENDGAME_SCORE)
                ||
                (resourceDeck.isTopEmpty() && goldDeck.isTopEmpty())
                ;
    }

    /**
     * @return true if there is at least one card to draw on the board (among revealed cards or top deck)
     */
    public boolean canDraw(){
        return !(resourceDeck.isCompletelyEmpty() && goldDeck.isCompletelyEmpty());
    }

    /**
     * Places the given card on the given corner on player's playArea <br>
     * Also removes the given card from player's hand
     * @param player the player doing the placement action
     * @param card the card to be placed
     * @param corner the corner on which to place card
     * @throws IllegalArgumentException if player isn't in game, card isn't in player's hand or corner is occupied
     * @throws IllegalStateException if the placement is invalid (as per PlayArea.placeCard())
     */
    public void placeCard(Player player, PlayCard card, Corner corner) throws IllegalArgumentException, IllegalStateException{
        //checks
        if(!playerAreas.containsKey(player)) throw new IllegalArgumentException("Player not in this game!");
        if(!player.getHand().containsCard(card)) throw new IllegalArgumentException("Card not in player's hand!");

        //placement
        PlayArea playArea = playerAreas.get(player);
        PlayCard placedCard = playArea.placeCard(card, corner); // throws IllegalStateException if the placement is invalid
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

    /**
     * Deals cards from the specified deck to player's hand <br>
     * If deal() is called for dealing the objective cards, then two objective cards are dealt. <br>
     * If deal() is called for dealing the starting card, then one starting card is dealt.
     * @param deck A character indicating the deck from which to deal the cards
     *             <br> it is advised to use Board.STARTING_DECK or Board.OBJECTIVE_DECK constants to select the deck
     * @param playerHand which hand to deal the card(s) to
     * @throws IllegalStateException if the deck specified isn't Board.STARTING_DECK or Board.OBJECTIVE_DECK <br>
     *                              or if dealing the requested cards to player's hand is an invalid action
     * @throws DeckException if the specified deck is empty
     */
    public void deal(char deck, PlayerHand playerHand) throws IllegalStateException, DeckException {
        //TODO [Gamba] choose whether to handle the deck exception
        switch (deck){
            case STARTING_DECK:
                StartingCard startingCard = startingDeck.getCard();
                try {
                    playerHand.setStartingCard(startingCard);
                }catch (PlayerHandException e){
                    startingDeck.putCard(startingCard);
                    throw e;
                }
                break;
            case OBJECTIVE_DECK:
                ObjectiveCard objCard1 = objectiveDeck.getCard();
                ObjectiveCard objCard2 = objectiveDeck.getCard();
                try {
                    playerHand.setObjectiveCard(objCard1);   // hand will never have only one objective card with MAX_OBJECTIVES = 2
                    playerHand.setObjectiveCard(objCard2);   // so separating this is not necessary. The second can't throw if the first doesn't throw
                }catch (PlayerHandException e){
                    objectiveDeck.putCard(objCard1);
                    objectiveDeck.putCard(objCard2);
                    throw e;
                }
                break;
            default:
                throw new IllegalStateException("Choosing a non-dealable deck");
        }
    }

    /**
     * @return A list of the 2 revealed objectives on the Board
     */
    public List<ObjectiveCard> getRevealedObjectives(){
        List<ObjectiveCard> revealedObj = new ArrayList<>();
        revealedObj.add(objectiveDeck.getFirstRevealed());
        revealedObj.add(objectiveDeck.getSecondRevealed());
        return revealedObj;
    }

    /**
     * Draws one card from the top of the specified deck
     * @param deck the deck from which to draw
     * @param playerHand the hand where the drawn card will be put
     * @throws IllegalStateException if playerHand is full (already has 3 cards) or if the specified deck isn't Board.RESOURCE_DECK or Board.GOLD_DECK
     * @throws DeckException if the specified deck is empty
     * @throws PlayerHandException if the card drawn is (for some error/bug) already in playerHand
     */
    public void drawTop(char deck, PlayerHand playerHand) throws IllegalStateException, DeckException, PlayerHandException {
        if(playerHand.isHandFull())
            throw new IllegalStateException("Player hand is full. Can't draw");

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
    /**
     * Draws the first card revealed in front of the specified deck. <br>
     * Automatically reveals a card to replace the one that was drawn
     * @param deck the deck from which to draw
     * @param playerHand the hand where the drawn card will be put
     * @throws IllegalStateException if playerHand is full (already has 3 cards) or if the specified deck isn't Board.RESOURCE_DECK or Board.GOLD_DECK
     * @throws DeckException if the specified deck is empty
     * @throws PlayerHandException if the card drawn is (for some error/bug) already in playerHand
     */
    public void drawFirst(char deck, PlayerHand playerHand) throws IllegalStateException, DeckException, PlayerHandException {
        if(playerHand.isHandFull())
            throw new IllegalStateException("Player hand is full. Can't draw");

        switch (deck){
            case RESOURCE_DECK:
                playerHand.addCard(resourceDeck.getFirstRevealedCard());
                break;
            case GOLD_DECK:
                playerHand.addCard(goldDeck.getFirstRevealedCard());
                break;
            default:
                throw new IllegalStateException("Choosing a non-drawable deck");
        }
    }
    /**
     * Draws the second card revealed in front of the specified deck. <br>
     * Automatically reveals a card to replace the one that was drawn
     * @param deck the deck from which to draw
     * @param playerHand the hand where the drawn card will be put
     * @throws IllegalStateException if playerHand is full (already has 3 cards) or if the specified deck isn't Board.RESOURCE_DECK or Board.GOLD_DECK
     * @throws DeckException if the specified deck is empty
     * @throws PlayerHandException if the card drawn is (for some error/bug) already in playerHand
     */
    public void drawSecond(char deck, PlayerHand playerHand) throws IllegalStateException, DeckException, PlayerHandException {
        if(playerHand.isHandFull())
            throw new IllegalStateException("Player hand is full. Can't draw");

        switch (deck){
            case RESOURCE_DECK:
                playerHand.addCard(resourceDeck.getSecondRevealedCard());
                break;
            case GOLD_DECK:
                playerHand.addCard(goldDeck.getSecondRevealedCard());
                break;
            default:
                throw new IllegalStateException("Choosing a non-drawable deck");
        }
    }

    /**
     * @param nickname player's nickname
     * @return true if the player is in this game
     */
    public boolean containsPlayer(String nickname) {
        return playerAreas.keySet().stream()
                .anyMatch(p -> p.getNickname().equals(nickname));
    }

    /**
     * @param nickname player's nickname
     * @return the Player instance of the player with given nickname
     * @throws IllegalStateException if there is no player in this game with the given nickname
     */
    public Player getPlayerByNickname(String nickname) throws IllegalArgumentException{
        return playerAreas.keySet().stream()
                .filter(p -> p.getNickname().equals(nickname))
                .findFirst().orElseThrow(()-> new IllegalArgumentException("Cannot find a player with given nickname in this game") );
    }

    /**
     * Removes the player with given nickname from the game (deleting all his information)
     * @param nickname player's nickname
     * @throws IllegalStateException if there is no player in this game with the given nickname
     */
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
