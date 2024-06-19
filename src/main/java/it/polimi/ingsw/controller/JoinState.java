package it.polimi.ingsw.controller;

import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.model.listener.remote.errors.IllegalActionError;
import it.polimi.ingsw.network.VirtualClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the game state during which players are joining the game.
 */
public class JoinState extends GameState {
    int numOfPlayersToStart;
    private static final int PING_TIME = 5;
    private static final int JOIN_TIMEOUT = 60*60; // 1 hour

    /**
     * Constructs a JoinState object with the specified parameters.
     * @param board the game board
     * @param controller the board controller
     * @param disconnectingPlayers the list of disconnecting players
     * @param num the number of players required to start the game
     */
    public JoinState(Board board, BoardController controller, List<String> disconnectingPlayers, int num){
        super(board, controller, disconnectingPlayers);
        numOfPlayersToStart = num;
        board.setGamePhase(GamePhase.JOIN);
        board.squashHistory();
        timers.startAll(board.getPlayerAreas().keySet().stream().toList(), JOIN_TIMEOUT, PING_TIME);
    }

    /**
     * Handles a player joining the game.
     * @throws IllegalStateException if the operation is not allowed in the current game state
     */
    @Override
    public void join(String nickname, VirtualClient client) throws IllegalStateException {
        if(board.getGamePhase()!=GamePhase.JOIN)
            throw new IllegalStateException("IMPOSSIBLE TO JOIN IN THIS PHASE");
        Player joiningPlayer = new Player(nickname);
        board.addPlayer(joiningPlayer);
        board.subscribeClientToUpdates(nickname, client);
        if(!disconnectingPlayers.isEmpty()) return;
        timers.startTimer(joiningPlayer, JOIN_TIMEOUT, PING_TIME);
        if(board.getPlayerAreas().size() == numOfPlayersToStart) {
            transition(new SetupState(board, controller, disconnectingPlayers));
        }
    }

    /**
     * Handles a player disconnecting from the game.
     * @throws IllegalArgumentException if the specified player nickname is invalid
     */
    @Override
    public void disconnect(String nickname) throws IllegalArgumentException {
        disconnectingPlayers.remove(nickname);

        board.unsubscribeClientFromUpdates(nickname);
        timers.stopTimer(board.getPlayerByNickname(nickname));
        board.removePlayer(nickname);
        if(board.getPlayerAreas().isEmpty())
            transition(new CreationState(new Board(), controller, new ArrayList<>()));
    }

    /**
     * Notifies that it's not allowed to change the number of players during the join state.
     * @throws IllegalStateException if the operation is not allowed in the current game state
     */
    @Override
    public void setNumOfPlayers(String nickname, int num) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO CHANGE THE NUMBER OF PLAYERS DURING JOIN STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO CHANGE THE NUMBER OF PLAYERS DURING JOIN STATE");
    }

    /**
     * Notifies that it's not allowed to place starting cards during the join state.
     * @throws IllegalStateException if the operation is not allowed in the current game state
     */
    @Override
    public void placeStartingCard(String nickname, boolean placeOnFront) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO PLACE STARTING CARDS DURING JOIN STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO PLACE STARTING CARDS DURING JOIN STATE");
    }

    /**
     * Notifies that it's not allowed to choose a color during the join state.
     * @throws IllegalStateException if the operation is not allowed in the current game state
     */
    public void chooseYourColor(String nickname, PlayerColor color) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO CHOOSE YOUR COLOR DURING CREATION STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO CHOOSE YOUR COLOR DURING CREATION STATE");
    }

    /**
     * Notifies that it's not allowed to choose a secret objective during the join state.
     * @throws IllegalStateException if the operation is not allowed in the current game state
     */
    @Override
    public void chooseSecretObjective(String nickname, int choice) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO CHOOSE SECRETE OBJECTIVE DURING JOIN STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO CHOOSE SECRETE OBJECTIVE DURING JOIN STATE");
    }

    /**
     * Notifies that it's not allowed to draw starting cards during the join state.
     * @throws IllegalStateException if the operation is not allowed in the current game state
     */
    @Override
    public void draw(String nickname, char deckFrom, int cardPos) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname,"IMPOSSIBLE TO DRAW STARTING CARDS DURING JOIN STATE" ));
        throw new IllegalStateException("IMPOSSIBLE TO DRAW STARTING CARDS DURING JOIN STATE");
    }

    /**
     * Notifies that it's not allowed to place cards during the join state.
     * @throws IllegalStateException if the operation is not allowed in the current game state
     */
    @Override
    public void placeCard(String nickname, String cardID, GamePoint cardPos, CornerDirection cornerDir, boolean placeOnFront) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO PLACE CARDS DURING JOIN STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO PLACE CARDS DURING JOIN STATE");
    }

    /**
     * Notifies about the attempt to restart the game during the join state, which is not allowed.
     * @throws IllegalStateException if an attempt is made to restart the game during the join state
     */
    @Override
    public void restartGame(String nickname, int numOfPlayers) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname,"IMPOSSIBLE TO START A NEW GAME DURING JOIN STATE" ));
        throw new IllegalStateException("IMPOSSIBLE TO START A NEW GAME DURING JOIN STATE");
    }
}
