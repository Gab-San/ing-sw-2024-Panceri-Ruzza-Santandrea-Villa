package it.polimi.ingsw.controller;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.PlayArea;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.exceptions.DeckException;
import it.polimi.ingsw.model.exceptions.PlayerHandException;
import it.polimi.ingsw.model.listener.remote.errors.IllegalActionError;
import it.polimi.ingsw.model.listener.remote.errors.IllegalParameterError;
import it.polimi.ingsw.network.VirtualClient;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Represents the play state in the game where players take turns to place and draw cards.
 * Handles player actions and transitions between phases.
 */
public class PlayState extends GameState {
    private boolean lastRound;
    private boolean currentPlayerHasPlacedCard;
    private Timer disconnectRejoinTimer;
    private static final int TURN_TIME = 302; // seconds
    private static final int PLAYER_REJOIN_TIME = 120; //seconds
    private GamePhase phase_preWaitingRejoin;

    /**
     * Constructs a new instance of PlayState.
     * @param board the board representing the game state
     * @param controller the controller managing the game flow
     * @param disconnectingPlayers a list of players who disconnected during the game
     */
    public PlayState(Board board, BoardController controller, List<String> disconnectingPlayers) {
        super(board, controller, disconnectingPlayers);
        int startingTurn = board.getPlayersByTurn().stream()
                .filter(Player::isConnected)
                .mapToInt(Player::getTurn)
                .min().orElse(-1); // there should always be at least one connected player at this point
        board.setCurrentTurn(startingTurn);
        lastRound = false;
        currentPlayerHasPlacedCard = false;
        board.setGamePhase(GamePhase.PLACECARD);
        board.squashHistory();

        if(board.isOnePlayerRemaining()){
            startRejoinTimer();
        }
        else{
            Player player = board.getCurrentPlayer(); //never throws IllegalState
            timers.startTimer(player, TURN_TIME);
        }
    }

    /**
     * Handles the player joining the game during the play state.
     * @throws IllegalStateException if the game phase does not allow joining
     * @throws IllegalArgumentException if the player cannot be reconnected
     */
    @Override
    public void join(String nickname, VirtualClient client) throws IllegalStateException, IllegalArgumentException {
        board.reconnectPlayer(nickname);

        if(board.getGamePhase() == GamePhase.WAITING_FOR_REJOIN){
            resumePlay();
        }

        board.subscribeClientToUpdates(nickname, client);
    }

    /**
     * Handles the player disconnecting from the game during the play state.
     * @throws IllegalStateException if the game phase does not allow disconnecting
     * @throws IllegalArgumentException if the player is not in the game
     */
    @Override
    public void disconnect(String nickname) throws IllegalStateException, IllegalArgumentException {
        disconnectingPlayers.remove(nickname);

        board.getPlayerByNickname(nickname); // throws IllegalArgumentException if player not in game

        board.unsubscribeClientFromUpdates(nickname);
        board.disconnectPlayer(nickname);
        Player player = board.getCurrentPlayer();

        timers.stopTimer(player);

        if(board.isOnePlayerRemaining()) // pause game if only one player is left
            startRejoinTimer();
        else if(player.getNickname().equals(nickname))
            postDrawChecks(); //if the game can continue and disconnecting player was playing, end their turn

        if(board.getNumOfConnectedPlayers() == 0) {
            stopRejoinTimer();
            transition(new CreationState(new Board(), controller, disconnectingPlayers));
        }
    }

    /**
     * Sets the number of players for the game. Not allowed during the play state.
     * @throws IllegalStateException always, because changing the number of players is not allowed during the play state
     */
    @Override
    public void setNumOfPlayers(String nickname, int num) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO CHANGE THE NUMBER OF PLAYERS DURING PLAY STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO CHANGE THE NUMBER OF PLAYERS DURING PLAY STATE");
    }

    /**
     * Places a starting card for a player. Not allowed during the play state.
     * @throws IllegalStateException always, because placing starting cards is not allowed during the play state
     */
    @Override
    public void placeStartingCard(String nickname, boolean placeOnFront) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname,"IMPOSSIBLE TO PLACE STARTING CARD DURING PLAY STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO PLACE STARTING CARD DURING PLAY STATE");
    }
    /**
     * Allows a player to choose or change their color. Not allowed during the play state.
     * @throws IllegalStateException always, because choosing or changing color is not allowed during the play state
     */
    public void chooseYourColor(String nickname, PlayerColor color) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO CHOOSE OR CHANGE YOUR COLOR DURING PLAY STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO CHOOSE OR CHANGE YOUR COLOR DURING PLAY STATE");
    }

    /**
     * Allows a player to choose a secret objective. Not allowed during the play state.
     * @throws IllegalStateException always, because choosing a secret objective is not allowed during the play state
     */
    @Override
    public void chooseSecretObjective(String nickname, int choice) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO CHOOSE SECRET OBJECTIVE DURING PLAY STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO CHOOSE SECRET OBJECTIVE DURING PLAY STATE");
    }

    /**
     * Places a card on the board for the current player.
     * @throws IllegalStateException if the card cannot be placed in the current phase or if it is not the player's turn
     * @throws IllegalArgumentException if the card position or corner is invalid
     */
    @Override
    public void placeCard(String nickname, String cardID, GamePoint cardPos, CornerDirection cornerDir, boolean placeOnFront)
            throws IllegalStateException, IllegalArgumentException {

        Player player;
        player = board.getPlayerByNickname(nickname); // throws if player isn't in game

        if(board.getGamePhase() != GamePhase.PLACECARD) {
            board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO PLACE A CARD IN THIS PHASE"));
            throw new IllegalStateException("IMPOSSIBLE TO PLACE A CARD IN THIS PHASE");
        }
        // In theory this if should never be called
        if (currentPlayerHasPlacedCard) {
            board.notifyAllListeners(new IllegalActionError(nickname, "Player had already placed a card!".toUpperCase()));
            throw new IllegalStateException("Player had already placed a card!");
        }

        if(!board.getCurrentPlayer().equals(player)) {
            board.notifyAllListeners(new IllegalActionError(nickname, "It's not your turn to place the card yet".toUpperCase() ));
            throw new IllegalStateException("It's not your turn to place the card yet");
        }

        PlayArea playerPlayArea = board.getPlayerAreas().get(player);
        Corner corner;
        try {
             corner = playerPlayArea.getCardMatrix().get(cardPos).getCorner(cornerDir);
        } catch (NullPointerException pointerException){
            board.notifyAllListeners(new IllegalParameterError(nickname, "TRYING TO PLACE ON A NON EXISTENT CORNER"));
            throw new IllegalArgumentException("THE DESIRED CORNER WAS NOT FOUND");
        }

        PlayCard cardToPlace = player.getHand().getCardByID(cardID);

        if(placeOnFront) cardToPlace.turnFaceUp();
        else cardToPlace.turnFaceDown();

        board.placeCard(player, cardToPlace, corner);
        board.checkEndgame(); //only useful to send notification

        if(board.canDraw()){
            currentPlayerHasPlacedCard = true;
            board.setGamePhase(GamePhase.DRAWCARD);
        }
        else{
            timers.stopTimer(player);
            postDrawChecks();
        }
    }

    /**
     * Draws a card from the specified deck for the current player.
     * @throws IllegalStateException if the draw cannot be performed in the current phase or if it is not the player's turn
     * @throws IllegalArgumentException if the card position is invalid or the draw cannot be completed
     */
    @Override
    public void draw(String nickname, char deckFrom, int cardPos)
            throws IllegalStateException, IllegalArgumentException {

        if(board.getGamePhase() != GamePhase.DRAWCARD) {
            board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO DRAW IN THIS PHASE"));
            throw new IllegalStateException("IMPOSSIBLE TO DRAW IN THIS PHASE");
        }

        Player player = board.getPlayerByNickname(nickname);

        if(!board.getCurrentPlayer().equals(player)) {
            board.notifyAllListeners(new IllegalActionError(nickname, "It's not your turn to draw yet".toUpperCase()));
            throw new IllegalStateException("It's not your turn to draw yet");
        }

        timers.stopTimer(player);

        try {
            switch (cardPos) {
                case 0:
                    board.drawTop(deckFrom, player.getHand());
                    break;
                case 1:
                    board.drawFirst(deckFrom, player.getHand());
                    break;
                case 2:
                    board.drawSecond(deckFrom, player.getHand());
                    break;
                default:
                    board.notifyAllListeners(new IllegalParameterError(nickname, "Invalid card position for this draw command.".toUpperCase()));
                    throw new IllegalArgumentException("Invalid card position for this draw command.");
            }
        }catch (DeckException |PlayerHandException | IllegalStateException e){
            throw new IllegalArgumentException("Can't draw from position " + cardPos, e);
        }

        postDrawChecks();
    }

    /**
     * Checks the state of the game after a player draws a card, updates game phases and transitions if necessary.
     */
    private void postDrawChecks(){
        int lastPlayerTurn = board.getPlayerAreas().keySet().stream()
        // only look at players that are connected and NOT deadlocked
                .filter(p -> p.isConnected() && !board.getPlayerDeadlocks().get(p))
                .mapToInt(Player::getTurn)
                .max()
                .orElse(0); // if all players are deadlocked/disconnected, this doesn't matter

        boolean isLastPlayerTurn = board.getCurrentTurn() >= lastPlayerTurn;
        if(lastRound && isLastPlayerTurn) {
            nextState();
            return;
        }

        if(board.checkEndgame() && isLastPlayerTurn)
            lastRound=true;

        if(board.nextTurn()) {
            // if a player disconnected after place card but before draw:
            //      that player skips the updatePlaceCard step (if he can draw)
            currentPlayerHasPlacedCard = board.canDraw() && !board.getCurrentPlayer().getHand().isHandFull();
            if(currentPlayerHasPlacedCard)
                board.setGamePhase(GamePhase.DRAWCARD);
            else
                board.setGamePhase(GamePhase.PLACECARD);

            timers.startTimer(board.getCurrentPlayer(), TURN_TIME);
        }
        else nextState();
    }
    /**
     * Transitions the game to the next state, which is the endgame state.
     * @throws IllegalStateException if the transition to the next state is not possible
     */
    private void nextState() throws IllegalStateException {
        stopRejoinTimer();
        transition( new EndgameState(board, controller, disconnectingPlayers) );
    }

    /**
     * Restarts the game. Not allowed during the play state.
     * @throws IllegalStateException always, because restarting the game is not allowed during the play state
     */
    @Override
    public void restartGame(String nickname, int numOfPlayers) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO START GAME DURING PLAY STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO START GAME DURING PLAY STATE");
    }

    /**
     * Starts a timer to wait for players to rejoin if only one player remains connected.
     */
    private void startRejoinTimer(){
        if(disconnectRejoinTimer == null){ //if not already waiting for rejoin
            disconnectRejoinTimer = new Timer();
            disconnectRejoinTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    nextState();
                }
            }, PLAYER_REJOIN_TIME * 1000L);
            phase_preWaitingRejoin = board.getGamePhase();
            timers.stopTimer(board.getCurrentPlayer());
            board.setGamePhase(GamePhase.WAITING_FOR_REJOIN);
        }
    }
    /**
     * Stops the rejoin timer if it is running.
     */
    private void stopRejoinTimer(){
        if(disconnectRejoinTimer != null){
            disconnectRejoinTimer.cancel();
            disconnectRejoinTimer = null;
        }
    }
    /**
     * Resumes play from where it was paused for player rejoining.
     */
    private void resumePlay(){
        stopRejoinTimer();
        board.setGamePhase(phase_preWaitingRejoin);
        if(board.getCurrentPlayer().isConnected())
            timers.startTimer(board.getCurrentPlayer(), TURN_TIME);
        else postDrawChecks();
        //if the current player isn't connected but the game can continue (>2 players), skip the turn
    }
}
