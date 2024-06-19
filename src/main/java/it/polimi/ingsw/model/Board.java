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
import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.model.exceptions.DeckException;
import it.polimi.ingsw.model.exceptions.ListenException;
import it.polimi.ingsw.model.exceptions.PlayerHandException;
import it.polimi.ingsw.model.listener.GameEvent;
import it.polimi.ingsw.model.listener.GameListener;
import it.polimi.ingsw.model.listener.GameSubject;
import it.polimi.ingsw.model.listener.remote.RemoteErrorHandler;
import it.polimi.ingsw.model.listener.remote.RemoteHandler;
import it.polimi.ingsw.model.listener.remote.errors.CrashStateError;
import it.polimi.ingsw.model.listener.remote.errors.IllegalGameAccessError;
import it.polimi.ingsw.model.listener.remote.errors.IllegalParameterError;
import it.polimi.ingsw.model.listener.remote.errors.IllegalStateError;
import it.polimi.ingsw.model.listener.remote.events.UpdateEvent;
import it.polimi.ingsw.model.listener.remote.events.board.*;
import it.polimi.ingsw.model.listener.remote.events.deck.DeckStateUpdateEvent;
import it.polimi.ingsw.model.listener.remote.events.playarea.PlayAreaStateUpdate;
import it.polimi.ingsw.model.listener.remote.events.player.PlayerDeadLockedEvent;
import it.polimi.ingsw.model.listener.remote.events.player.PlayerRemovalEvent;
import it.polimi.ingsw.model.listener.remote.events.player.PlayerStateUpdateEvent;
import it.polimi.ingsw.model.listener.remote.events.playerhand.PlayerHandSetStartingCardEvent;
import it.polimi.ingsw.model.listener.remote.events.playerhand.PlayerHandStateUpdateEvent;
import it.polimi.ingsw.network.CentralServer;
import it.polimi.ingsw.network.VirtualClient;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The main class of the model. <br>
 * Represents the central board visible by all players. <br>
 * Acts as the first point of access to the model.
 */
public class Board implements GameSubject{
    /**
     * The score that triggers the endgame (last rounds)
     */
    public static final int ENDGAME_SCORE = 20;
    /**
     * The player score limit during PlayState
     */
    public static final int MAX_PLAY_SCORE = 29;
    /**
     * The max number of players in a game
     */
    public static final int MAX_PLAYERS = 4;
    /**
     * True if the endgame notification has already been sent to Clients
     */
    private boolean hasGoneToEndgame;
    /**
     * Map containing Player - score pairings
     */
    private final Map<Player, Integer> scoreboard;
    /**
     * Map containing Player - PlayArea pairings
     */
    private final Map<Player, PlayArea> playerAreas;

    private final PlayableDeck resourceDeck, goldDeck;
    private final ObjectiveDeck objectiveDeck;
    private final StartingCardDeck startingDeck;
    public static final char STARTING_DECK = 'S';
    public static final char OBJECTIVE_DECK = 'O';
    public static final char RESOURCE_DECK = 'R';
    public static final char GOLD_DECK = 'G';

    /**
     * Map containing Player - Deadlock pairings
     */
    private final Map<Player, Boolean> isPlayerDeadlocked;

    private int currentTurn;
    private GamePhase gamePhase;

//region LISTENER ATTRIBUTES
    private final List<GameSubject> observableObjects;
    private final List<GameListener> gameListeners;
    private final RemoteHandler remoteHandler;
    private final RemoteErrorHandler errorHandler;
//endregion

    /**
     * Constructs the Board (as in initializing the game)
     * * @throws IllegalStateException if an error occurs while initializing board
     */
    public Board() throws IllegalStateException{
        observableObjects = new LinkedList<>();
        gameListeners = new LinkedList<>();
        hasGoneToEndgame = false;
        // Controlled in Board
        setCurrentTurn(1);
        // Controlled in Board
        scoreboard = new Hashtable<>();
        // Probably should notify
        playerAreas = new Hashtable<>();
        // Controlled in Board
        gamePhase = GamePhase.CREATE;

        try {
            resourceDeck = new PlayableDeck(Board.RESOURCE_DECK, new ResourceCardFactory(), 8);

        } catch (IllegalStateException exception){
            notifyAllListeners(new CrashStateError("all", "An error occured while initializing the resource deck".toUpperCase()));
            System.exit(-1);
            throw new IllegalStateException(exception.getMessage());
        }

        try {
            goldDeck = new PlayableDeck(Board.GOLD_DECK, new GoldCardFactory(), 5);
        } catch (IllegalStateException exception){
            notifyAllListeners(new CrashStateError("all", "An error occured while initializing the gold deck".toUpperCase()));
            System.exit(-1);
            throw new IllegalStateException(exception.getMessage());
        }

        observableObjects.add(resourceDeck);
        observableObjects.add(goldDeck);
        objectiveDeck = new ObjectiveDeck();
        observableObjects.add(objectiveDeck);

        try {
            startingDeck = new StartingCardDeck();
        } catch (IllegalStateException e){
            notifyAllListeners(new CrashStateError("all", "An error occured while initializing the starting deck".toUpperCase()));
            System.exit(-1);
            throw e;
        }
        // Controlled in Board
        isPlayerDeadlocked = new Hashtable<>();
        observableObjects.add(this);
        remoteHandler = new RemoteHandler();
        errorHandler = new RemoteErrorHandler();
        subscribeListenerToAll(remoteHandler);
        subscribeListenerToAll(errorHandler);

        notifyAllListeners(new BoardStateUpdateEvent(currentTurn, scoreboard, gamePhase, isPlayerDeadlocked));
        notifyAllListeners(new DeckStateUpdateEvent(Board.OBJECTIVE_DECK, objectiveDeck.peekTop(), objectiveDeck.getFirstRevealed(), objectiveDeck.getSecondRevealed()));
        notifyAllListeners(new DeckStateUpdateEvent(Board.RESOURCE_DECK, resourceDeck.peekTop(),
                resourceDeck.peekFirst(), resourceDeck.peekSecond()));
        notifyAllListeners(new DeckStateUpdateEvent(Board.GOLD_DECK, goldDeck.peekTop(),
                goldDeck.peekFirst(), goldDeck.peekSecond()));
    }


    /**
     * Constructs the board (as in initializing the game) and automatically makes the given players join
     * @param players list of 1-4 players that are joining this game
     * @throws InvalidParameterException if the list of players has an illegal player count (0 or >4)
     * @throws IllegalStateException if players contains duplicates or an error occurs while initializing board
     */
    public Board(Board oldBoard, List<Player> players) throws InvalidParameterException, IllegalStateException{
        this();
        if(players.isEmpty() || players.size() > MAX_PLAYERS) throw new InvalidParameterException("Illegal number of players!");
        for(Player p: players){
            addPlayer(new Player(p.getNickname()));
        }
        Map<String, VirtualClient> connectedPlayers = oldBoard.remoteHandler.getClients();
        for(String username : connectedPlayers.keySet()){
            subscribeClientToUpdates(username, connectedPlayers.get(username));
        }
    }

//region TURN METHODS
    public int getCurrentTurn() {
        return currentTurn;
    }
    public void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
        notifyAllListeners(new ChangeTurnEvent(currentTurn));
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
            if(currentTurn >= playerAreas.size()) {
                squashHistory();
                setCurrentTurn(1);
            } else {
                int nextTurn = currentTurn + 1;
                setCurrentTurn(nextTurn);
            }
            nextPlayer = playersByTurn.get(currentTurn-1);
        } while(!nextPlayer.isConnected() || isPlayerDeadlocked.get(nextPlayer));

        return true;
    }
//endregion

//region GAME STATUS METHODS
    public GamePhase getGamePhase() {
        return gamePhase;
    }

//endregion

//region GETTERS
    public Map<Player, PlayArea> getPlayerAreas(){
        return playerAreas;
    }
    public Map<Player, Boolean> getPlayerDeadlocks(){
        return isPlayerDeadlocked;
    }

    public Map<Player, Integer> getScoreboard(){
        return scoreboard;
    }

    /**
     * @return Set of all colors that have not yet been chosen by a player
     */
    public Set<PlayerColor> getAvailableColors(){
        Set<PlayerColor> colors = Arrays.stream(PlayerColor.values()).collect(Collectors.toSet());
        for(Player p : playerAreas.keySet()){
            if(p.getColor() != null)
                colors.remove(p.getColor());
        }
        return colors;
    }

    /**
     * @return a random color among colors that have not yet been chosen by a player
     */
    public PlayerColor getRandomAvailableColor() throws IllegalStateException{
        Set<PlayerColor> colors = getAvailableColors();
        return colors.stream().unordered().findFirst().orElseThrow(()->new IllegalStateException("No player colors are available"));
    }
//endregion

//region BOARD SETTER METHODS

    /**
     * Adds points to player's score
     * @param player player to whom the points will be awarded
     * @param amount number of points to award
     * @throws IllegalArgumentException if the player isn't in game
     */
    public void addScore(Player player, int amount) throws IllegalArgumentException{
        if(!scoreboard.containsKey(player)){
            notifyAllListeners(new IllegalGameAccessError(player.getNickname(), "Player not in game!".toUpperCase()));
            throw new IllegalArgumentException("Player not in game!");
        }

        int DEBUG_MODE_SCORE_MULT = 10;
        int newScore = scoreboard.get(player) + amount * (CentralServer.isPointsDebugMode() ? DEBUG_MODE_SCORE_MULT : 1);
        setScore(player, newScore);
    }
    protected void setScore(Player player, int score){
        if(gamePhase != GamePhase.EVALOBJ && score > MAX_PLAY_SCORE){
            score = MAX_PLAY_SCORE;
        }
        scoreboard.put(player, score);
        notifyAllListeners(new ChangeScoreEvent(player.getNickname(), score));
    }

    public void setGamePhase(GamePhase gamePhase) {
        this.gamePhase = gamePhase;
        notifyAllListeners(new ChangePhaseEvent(gamePhase));
    }
//endregion

    /**
     * @return true if any player has a score >= ENDGAME_SCORE or if both decks are empty <br>
     */
    public boolean checkEndgame(){
        if(scoreboard.values().stream().anyMatch(score -> score >= ENDGAME_SCORE)){
            List<Player> playersByScore = getPlayersByScore();
            Player highestScorePlayer = playersByScore.get(0);
            if(!hasGoneToEndgame) {
                hasGoneToEndgame = true;
                notifyAllListeners(new EndgameCheckEvent(highestScorePlayer.getNickname()
                        , scoreboard.get(highestScorePlayer)));
            }
            return true;
        }

        if(resourceDeck.isEmpty() && goldDeck.isEmpty()){
            if(!hasGoneToEndgame) {
                hasGoneToEndgame = true;
                notifyAllListeners(new EndgameCheckEvent());
            }
            return true;
        }
        return false;
    }

    public void setPlayerDeadLock(Player player, boolean isDeadLocked){
        isPlayerDeadlocked.put(player, isDeadLocked);
        notifyAllListeners(new PlayerDeadLockedEvent(player.getNickname(), isDeadLocked));
    }

//region DECKS METHODS
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
     * @throws IllegalStateException if the placement is invalid (as per PlayArea.updatePlaceCard())
     */
    public void placeCard(Player player, PlayCard card, Corner corner) throws IllegalArgumentException, IllegalStateException{
        if(!playerAreas.containsKey(player)){
            notifyAllListeners(new IllegalGameAccessError(player.getNickname(),"Player not in this game!".toUpperCase()));
            throw new IllegalArgumentException("Player not in this game!");
        }
        if(!player.getHand().containsCard(card)){
            notifyAllListeners(new IllegalParameterError(player.getNickname(),"Card not in player's hand!".toUpperCase()));
            throw new IllegalArgumentException("Card not in player's hand!");
        }

        //placement
        PlayArea playArea = playerAreas.get(player);
        PlayCard placedCard = playArea.placeCard(card, corner); // throws IllegalStateException if the placement is invalid
        player.getHand().removeCard(card);

        //scoreboard displayMessage
        addScore(player, placedCard.calculatePointsOnPlace(playArea));
    }
    /**
     * Places the player's starting card on their playArea
     * @param player the player doing the placement action
     * @param placeOnFront the facing on which to place the player's starting card
     * @throws IllegalStateException if the action is invalid (e.g. if the player hasn't received a startingCard yet or has already placed it)
     */
    public void placeStartingCard(Player player, boolean placeOnFront) throws IllegalStateException{
        StartingCard startingCard;
        try{
            startingCard = player.getHand().getStartingCard();
        }

        catch(PlayerHandException e){
            notifyAllListeners(new IllegalStateError(player.getNickname(), e.getMessage().toUpperCase()));
            throw new IllegalStateException("Player doesn't have a starting card yet!");
        }

        if(placeOnFront)
            startingCard.turnFaceUp();
        else
            startingCard.turnFaceDown();

        try {
            playerAreas.get(player).placeStartingCard(startingCard);
            notifyAllListeners(new PlayerHandSetStartingCardEvent(player.getNickname(), null));
        } catch (IllegalStateException stateException){
            notifyAllListeners(new IllegalStateError(player.getNickname(), stateException.getMessage().toUpperCase()));
            throw stateException;
        }
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
     */
    public void deal(char deck, PlayerHand playerHand) throws IllegalStateException{
        switch (deck){
            case STARTING_DECK:
                try {
                    playerHand.setStartingCard(startingDeck::getCard);
                } catch (PlayerHandException e){
                    Player p = playerHand.getPlayerRef();
                    notifyAllListeners(new IllegalStateError(p.getNickname(), e.getMessage()));
                    throw new IllegalStateException(e);
                }
                break;
            case OBJECTIVE_DECK:
                //Maybe since we set the objectiveCards only when dealing, and we have a lambda,
                //maybe we can use the lambda twice to draw two cards
                try {
                    playerHand.addObjectiveCard(objectiveDeck::getCard);   // hand will never have only one objective card with MAX_OBJECTIVES = 2
                    playerHand.addObjectiveCard(objectiveDeck::getCard);   // so separating this is not necessary.
                } catch (PlayerHandException e){
                    Player p = playerHand.getPlayerRef();
                    notifyAllListeners(new IllegalStateError(p.getNickname(), e.getMessage()));
                    throw new IllegalStateException(e);
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

        switch (deck){
            case RESOURCE_DECK:
                if(resourceDeck.isEmpty()){
                    notifyAllListeners(new IllegalStateError(playerHand.getPlayerRef().getNickname()
                            , "Deck is empty!".toUpperCase()));
                    throw new DeckException("Deck is empty!", PlayableDeck.class);
                }
                playerHand.addCard(resourceDeck::getTopCard);
                break;
            case GOLD_DECK:
                if(goldDeck.isEmpty()){
                    notifyAllListeners(new IllegalStateError(playerHand.getPlayerRef().getNickname()
                            , "Deck is empty!".toUpperCase()));
                    throw new DeckException("Deck is empty!", PlayableDeck.class);
                }
                playerHand.addCard(goldDeck::getTopCard);
                break;
            default:
                notifyAllListeners(new IllegalParameterError(playerHand.getPlayerRef().getNickname(), "Choosing a non-drawable deck".toUpperCase()));
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

        switch (deck){
            case RESOURCE_DECK:
                if(resourceDeck.isFirstRevealedEmpty()) {
                    notifyAllListeners(new IllegalStateError(playerHand.getPlayerRef().getNickname()
                            , "There is no first card!".toUpperCase()));
                    throw new DeckException("There is no first card!", PlayableDeck.class);
                }
                playerHand.addCard(resourceDeck::getFirstRevealedCard);
                break;
            case GOLD_DECK:
                if(goldDeck.isFirstRevealedEmpty()){
                    notifyAllListeners(new IllegalStateError(playerHand.getPlayerRef().getNickname()
                            , "There is no first card!".toUpperCase()));
                    throw new DeckException("There is no first card!", PlayableDeck.class);
                }
                playerHand.addCard(goldDeck::getFirstRevealedCard);
                break;
            default:
                notifyAllListeners(new IllegalParameterError(playerHand.getPlayerRef().getNickname(), "Choosing a non-drawable deck".toUpperCase()));
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

        switch (deck){
            case RESOURCE_DECK:
                if(resourceDeck.isSecondRevealedEmpty()) {
                    notifyAllListeners(new IllegalStateError(playerHand.getPlayerRef().getNickname()
                            , "There is no second card!".toUpperCase()));
                    throw new DeckException("There is no second card!", PlayableDeck.class);
                }
                playerHand.addCard(resourceDeck::getSecondRevealedCard);
                break;
            case GOLD_DECK:
                if(goldDeck.isSecondRevealedEmpty()){
                    notifyAllListeners(new IllegalStateError(playerHand.getPlayerRef().getNickname()
                            , "There is no second card!".toUpperCase()));
                    throw new DeckException("There is no second card!", PlayableDeck.class);
                }
                playerHand.addCard(goldDeck::getSecondRevealedCard);
                break;
            default:
                notifyAllListeners(new IllegalParameterError(playerHand.getPlayerRef().getNickname(), "Choosing a non-drawable deck".toUpperCase()));
                throw new IllegalStateException("Choosing a non-drawable deck");
        }
    }
    public void revealObjectives(){
        objectiveDeck.reveal();
    }

//endregion

//region PLAYER METHODS
    //region GETTERS
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
                .sorted(Comparator.comparingInt(p -> -scoreboard.get(p)))
                .toList();
    }

    /**
     * @return player whose turn corresponds to the currentTurn
     * @throws IllegalStateException if turns have not been assigned turns yet (no player has turned = currentTurn >= 1)
     */
    public Player getCurrentPlayer() throws IllegalStateException{
        return playerAreas.keySet().stream()
                .filter(p -> p.getTurn() == currentTurn)
                .findFirst().orElseThrow(()->{
                    notifyAllListeners(new IllegalStateError("all", "Players have not been assigned turns yet."));
                    return new IllegalStateException("Players have not been assigned turns yet.");
                });
    }

    /**
     * @param nickname player's nickname
     * @return the Player instance of the player with given nickname
     * @throws IllegalArgumentException if there is no player in this game with the given nickname
     */
    public Player getPlayerByNickname(String nickname) throws IllegalArgumentException{
        return playerAreas.keySet().stream()
                .filter(p -> p.getNickname().equals(nickname))
                .findFirst().orElseThrow(()->{
                    notifyAllListeners(new IllegalGameAccessError("all", ("Cannot find a player with given nickname " + "'" + nickname + "'" + " in this game").toUpperCase()) );
                    return new IllegalArgumentException("Cannot find a player with given nickname " + "'" + nickname + "'" + " in this game");
                });
    }

    /**
     * @return true if only one player is connected, false otherwise
     */
    public boolean isOnePlayerRemaining(){
        return getNumOfConnectedPlayers() == 1;
    }

    /**
     * @return (0-4) number of connected players in this game
     */
    public int getNumOfConnectedPlayers(){
        return (int) playerAreas.keySet().stream().filter(Player::isConnected).count();
    }

    //endregion
    /**
     * Adds a player to the game
     * @param player player that is joining the game
     * @throws IllegalStateException if the player had already joined or if the current game is full (4 players)
     */
    public void addPlayer(Player player) throws NullPointerException, IllegalStateException{
        if(player == null) throw new NullPointerException("Null player reference.");
        if(playerAreas.containsKey(player)) throw new IllegalStateException("Cannot add a player already in game.");
        if(playerAreas.size() >= MAX_PLAYERS) throw new IllegalStateException("Max number of players already in game.");
        // Player was already created
        // Listing player as observable object
        observableObjects.add(player);
        player.addListener(remoteHandler);
        player.addListener(errorHandler);
        notifyAllListeners(new PlayerStateUpdateEvent(player.getNickname(),
                player.isConnected(), player.getTurn(), player.getColor()));
        PlayerHand hand = player.getHand();
        hand.addListener(remoteHandler);
        hand.addListener(errorHandler);
        notifyAllListeners(new PlayerHandStateUpdateEvent(player.getNickname(), hand.peekCards(),
                hand.getObjectiveChoices(), hand.peekStartingCard()));

        // Setting score on scoreboard
        setScore(player, 0);
        // Creating PlayArea and listing as observable
        PlayArea joiningPlayerArea = new PlayArea(player.getNickname());
        observableObjects.add(joiningPlayerArea);
        joiningPlayerArea.addListener(remoteHandler);
        joiningPlayerArea.addListener(errorHandler);

        notifyAllListeners(new PlayAreaStateUpdate(player.getNickname(),
                joiningPlayerArea.getCardMatrix(), joiningPlayerArea.getVisibleResources(),
                joiningPlayerArea.getFreeCorners()));

        playerAreas.put(player, joiningPlayerArea);

        setPlayerDeadLock(player, false);
    }

    /**
     * Removes the player with given nickname from the game (deleting all his information)
     * Should only be called during Creation or Join states.
     * @param nickname player's nickname
     * @throws IllegalArgumentException if there is no player in this game with the given nickname
     */
    public void removePlayer(String nickname) throws IllegalArgumentException {
        Player player = getPlayerByNickname(nickname);
        // remove playArea and scoreboard
        playerAreas.remove(player);
        // Probably should notify
        scoreboard.remove(player);
        isPlayerDeadlocked.remove(player);
        //decrement turn of each player that followed the removed player in turn order
        playerAreas.keySet().stream()
                .filter(p -> p.getTurn() > player.getTurn())
                .forEach(p -> p.setTurn(p.getTurn()-1));

        notifyAllListeners(new PlayerRemovalEvent(nickname));

        player.removeListener(remoteHandler);
        player.removeListener(errorHandler);
        observableObjects.remove(player);
    }

    /**
     * Disconnects player with given nickname without removing their information from the board
     * @param nickname player's nickname
     * @throws IllegalArgumentException if there is no player with given nickname
     */
    public void disconnectPlayer(String nickname) throws IllegalArgumentException{
        getPlayerByNickname(nickname).setConnected(false);
    }
    /**
     * Reconnects player with given nickname
     * @param nickname player's nickname
     * @throws IllegalStateException if player is already connected
     * @throws IllegalArgumentException if there is no player with given nickname
     */
    public void reconnectPlayer(String nickname) throws IllegalStateException, IllegalArgumentException{
        Player player = getPlayerByNickname(nickname);
        if(player.isConnected())
            throw new IllegalStateException("Player " + nickname + " is already connected.");

        player.setConnected(true);
    }
//endregion

//region LISTENER METHODS
    void subscribeListenerToAll(GameListener listener){
        for(GameSubject subject: observableObjects){
            subject.addListener(listener);
        }
    }

    @Override
    public void addListener(GameListener listener) {
        synchronized (gameListeners) {
            gameListeners.add(listener);
        }
    }

    @Override
    public void removeListener(GameListener listener) {
        synchronized (gameListeners) {
            gameListeners.remove(listener);
        }
    }

    @Override
    public void notifyAllListeners(GameEvent event) throws ListenException {
        synchronized (gameListeners) {
            for (GameListener listener : gameListeners) {
                listener.listen(event);
            }
        }
    }

    public void subscribeClientToUpdates(String nickname, VirtualClient client){
        remoteHandler.addClient(nickname,client);
        errorHandler.addClient(nickname, client);
    }

    public void unsubscribeClientFromUpdates(String nickname){
        remoteHandler.removeClient(nickname);
        errorHandler.removeClient(nickname);
    }

    public void squashHistory(){
        List<UpdateEvent> stateSave = new ArrayList<>(16);

        stateSave.add(new BoardStateUpdateEvent(currentTurn, scoreboard, gamePhase, isPlayerDeadlocked));
        stateSave.add(new DeckStateUpdateEvent(Board.OBJECTIVE_DECK, objectiveDeck.peekTop(),
                objectiveDeck.getFirstRevealed(), objectiveDeck.getSecondRevealed()));
        stateSave.add(new DeckStateUpdateEvent(Board.RESOURCE_DECK, resourceDeck.peekTop(),
                resourceDeck.peekFirst(), resourceDeck.peekSecond()));
        stateSave.add(new DeckStateUpdateEvent(Board.GOLD_DECK, goldDeck.peekTop(),
                goldDeck.peekFirst(), goldDeck.peekSecond()));
        for(Player player : playerAreas.keySet()){
            stateSave.add(new PlayerStateUpdateEvent(player.getNickname(),
                    player.isConnected(), player.getTurn(), player.getColor()));
            PlayerHand hand = player.getHand();
            stateSave.add(new PlayerHandStateUpdateEvent(player.getNickname(), hand.peekCards(),
                    hand.getObjectiveChoices(), hand.peekStartingCard()));
            PlayArea playArea = playerAreas.get(player);
            stateSave.add(new PlayAreaStateUpdate(player.getNickname(),
                    playArea.getCardMatrix(), playArea.getVisibleResources(),
                    playArea.getFreeCorners()));
            stateSave.add(new PlayerHandSetStartingCardEvent(player.getNickname(),null));
        }

        remoteHandler.replaceHistory(stateSave);
        //adds the endgame notification after squash if needed
        if(hasGoneToEndgame){
            hasGoneToEndgame = false; //needed to actually send notification
            checkEndgame(); // resets hasGoneToEndgame
        }
    }

//endregion
}
