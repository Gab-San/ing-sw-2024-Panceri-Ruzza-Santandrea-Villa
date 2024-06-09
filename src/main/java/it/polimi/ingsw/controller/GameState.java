package it.polimi.ingsw.controller;

import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.controller.timer.TurnTimerController;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.network.VirtualClient;

import java.util.List;

/**
 * Represents the abstract state of the game.
 */
public abstract class GameState {
    protected final Board board;
    protected final BoardController controller;
    protected final List<String> disconnectingPlayers;
    protected final TurnTimerController timers;

    /**
     * Constructs a GameState object with the specified parameters.
     * @param board the game board
     * @param controller the board controller
     * @param disconnectingPlayers the list of disconnecting players
     */
    public GameState(Board board, BoardController controller, List<String> disconnectingPlayers) {
        this.board = board;
        this.controller = controller;
        this.disconnectingPlayers = disconnectingPlayers;
        this.timers = new TurnTimerController();
    }

    /**
     * Handles a player joining the game.
     * @param nickname the nickname of the joining player
     * @param client the virtual client of the joining player
     * @throws IllegalStateException if the operation is not allowed in the current game state
     */
    abstract public void join (String nickname, VirtualClient client) throws IllegalStateException;
    /**
     * Sets the number of players in the game.
     * @param nickname the nickname of the player initiating the action
     * @param num the number of players to set
     * @throws IllegalStateException if the operation is not allowed in the current game state
     * @throws IllegalArgumentException if the provided number of players is invalid
     */
    abstract public void setNumOfPlayers(String nickname, int num) throws IllegalStateException, IllegalArgumentException;

    /**
     * Handles a player disconnecting from the game.
     * @param nickname the nickname of the disconnecting player
     * @throws IllegalStateException if the operation is not allowed in the current game state
     * @throws IllegalArgumentException if the provided nickname is invalid
     */
    abstract public void disconnect (String nickname) throws IllegalStateException, IllegalArgumentException;
    /**
     * Handles placing a starting card on the game board.
     * @param nickname the nickname of the player placing the card
     * @param placeOnFront true if the card should be placed on the front side, false otherwise
     * @throws IllegalStateException if the operation is not allowed in the current game state
     * @throws IllegalArgumentException if the provided nickname is invalid
     */
    abstract public void placeStartingCard(String nickname, boolean placeOnFront) throws IllegalStateException, IllegalArgumentException;
    /**
     * Handles a player choosing their color for the game.
     * @param nickname the nickname of the player choosing the color
     * @param color the color chosen by the player
     * @throws IllegalStateException if the operation is not allowed in the current game state
     * @throws IllegalArgumentException if the provided nickname is invalid
     */
    abstract public void chooseYourColor(String nickname, PlayerColor color) throws IllegalStateException, IllegalArgumentException;

    /**
     * Handles a player choosing their secret objective.
     * @param nickname the nickname of the player choosing the objective
     * @param choice the choice made by the player
     * @throws IllegalStateException if the operation is not allowed in the current game state
     * @throws IllegalArgumentException if the provided nickname is invalid
     */
    abstract public void chooseSecretObjective(String nickname, int choice) throws IllegalStateException, IllegalArgumentException;
    /**
     * Handles placing a card on the game board.
     * @param nickname the nickname of the player placing the card
     * @param cardID the ID of the card to place
     * @param cardPos the position on the game board where the card will be placed
     * @param cornerDir the direction in which to place the card's corner
     * @param placeOnFront true if the card should be placed on the front side, false otherwise
     * @throws IllegalStateException if the operation is not allowed in the current game state
     * @throws IllegalArgumentException if the provided parameters are invalid
     */
    abstract public void placeCard(String nickname, String cardID, GamePoint cardPos, CornerDirection cornerDir, boolean placeOnFront) throws IllegalStateException, IllegalArgumentException;
    /**
     * Handles a player drawing a card from a deck.
     * @param nickname the nickname of the player drawing the card
     * @param deckFrom the deck from which the card is drawn
     * @param cardPos the position on the card
     * @throws IllegalStateException if the operation is not allowed in the current game state
     * @throws IllegalArgumentException if the provided parameters are invalid
     */
    abstract public void draw (String nickname, char deckFrom, int cardPos) throws IllegalStateException, IllegalArgumentException;
    /**
     * Handles restarting the game.
     * @param nickname the nickname of the player initiating the restart
     * @param numOfPlayers the number of players for the restarted game
     * @throws IllegalStateException if the operation is not allowed in the current game state
     * @throws IllegalArgumentException if the provided parameters are invalid
     */
    abstract public void restartGame (String nickname, int numOfPlayers) throws IllegalStateException, IllegalArgumentException;
    /**
     * Transitions to the next game state.
     * @param nextState the next game state to transition to
     */
    protected void transition(GameState nextState){
        timers.stopAll();
        controller.setGameState(nextState);
    }
}
