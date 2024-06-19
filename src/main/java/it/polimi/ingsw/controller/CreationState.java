package it.polimi.ingsw.controller;

import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.model.listener.remote.errors.IllegalActionError;
import it.polimi.ingsw.model.listener.remote.errors.IllegalParameterError;
import it.polimi.ingsw.network.VirtualClient;

import java.util.List;
import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.PlayerColor;

/**
 * The CreationState class represents the game state during the creation phase,
 * where players join and the game setup is done.
 */
public class CreationState extends GameState{
    private final int SET_TIME = 60; // 1 minute

    /**
     * Constructs a CreationState instance.
     * @param board the game board.
     * @param controller the board controller.
     * @param disconnectingPlayers the list of players who are disconnecting.
     */
    public CreationState(Board board, BoardController controller, List<String> disconnectingPlayers) {
        super(board, controller, disconnectingPlayers);
        board.setGamePhase(GamePhase.CREATE);
    }

    /**
     * Allows a player to join the game.
     * @throws IllegalStateException if players cannot join at this stage.
     */
    @Override
    public void join(String nickname, VirtualClient client) throws IllegalStateException {
        if(!board.getPlayerAreas().isEmpty()) {
            throw new IllegalStateException("WAITING FOR THE CONNECTED PLAYER TO START THE LOBBY...");
        }
        Player creator = new Player(nickname);
        board.addPlayer(creator);
        board.subscribeClientToUpdates(nickname, client);
        board.setGamePhase(GamePhase.SETNUMPLAYERS);
        timers.startTimer(creator, SET_TIME);
    }

    /**
     * Handles player disconnection.
     * @throws IllegalStateException if disconnection cannot be handled at this stage.
     */
    @Override
    public void disconnect(String nickname) throws IllegalArgumentException {
        disconnectingPlayers.remove(nickname);
        board.unsubscribeClientFromUpdates(nickname);
        timers.stopTimer(board.getPlayerByNickname(nickname));
        board.removePlayer(nickname);
        board.setGamePhase(GamePhase.CREATE);
        board.squashHistory();
    }

    /**
     * Sets the number of players for the game.
     * @throws IllegalArgumentException if the number of players is invalid.
     * @throws IllegalStateException if setting the number of players is not allowed at this stage.
     */
    @Override
    public void setNumOfPlayers(String nickname, int num)
                throws IllegalArgumentException, IllegalStateException{

        Player player = board.getPlayerByNickname(nickname); // throws on player not in game

        if(num<2 || num>4) {
            board.notifyAllListeners(new IllegalParameterError(nickname, "NUMBER OF PLAYERS IN THE GAME MUST BE BETWEEN 2 AND 4 INCLUDED, YOU INSERTED " + num + " PLAYERS" ));
            throw new IllegalArgumentException("NUMBER OF PLAYERS IN THE GAME MUST BE BETWEEN 2 AND 4 INCLUDED, YOU INSERTED " + num + " PLAYERS");
        }

        timers.stopTimer(player);
        transition(new JoinState(board, controller, disconnectingPlayers, num));
    }

    /**
     * Throws an exception because placing a starting card is not allowed during the creation state.
     * @throws IllegalStateException always, as this action is not allowed in this state.
     */
    @Override
    public void placeStartingCard(String nickname, boolean placeOnFront) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO PLACE A STARTING CARD DURING CREATION STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO PLACE A STARTING CARD DURING CREATION STATE");
    }

    /**
     * Throws an exception because choosing a color is not allowed during the creation state.
     * @throws IllegalStateException always, as this action is not allowed in this state.
     */
    @Override
    public void chooseYourColor(String nickname, PlayerColor color) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO CHOOSE YOUR COLOR DURING CREATION STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO CHOOSE YOUR COLOR DURING CREATION STATE");
    }

    /**
     * Throws an exception because choosing a secret objective is not allowed during the creation state.
     * @throws IllegalStateException always, as this action is not allowed in this state.
     */
    @Override
    public void chooseSecretObjective(String nickname, int choice) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO CHOOSE A SECRET OBJECTIVE DURING CREATION STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO CHOOSE A SECRET OBJECTIVE DURING CREATION STATE");
    }

    /**
     * Throws an exception because drawing is not allowed during the creation state.
     * @throws IllegalStateException always, as this action is not allowed in this state.
     */
    @Override
    public void draw(String nickname, char deckFrom, int cardPos) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO DRAW DURING CREATION STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO DRAW DURING CREATION STATE");
    }

    /**
     * Throws an exception because placing a card is not allowed during the creation state.
     * @throws IllegalStateException always, as this action is not allowed in this state.
     */
    @Override
    public void placeCard(String nickname, String cardID, GamePoint cardPos, CornerDirection cornerDir, boolean placeOnFront) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO PLACE A CARD DURING CREATION STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO PLACE A CARD DURING CREATION STATE");
    }

    /**
     * Throws an exception because restarting the game is not allowed during the creation state.
     * @throws IllegalStateException always, as this action is not allowed in this state.
     */
    @Override
    public void restartGame (String nickname, int numOfPlayers) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO START ANOTHER GAME DURING CREATION STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO START ANOTHER GAME DURING CREATION STATE");
    }
}
