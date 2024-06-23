package it.polimi.ingsw.controller;

import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.network.VirtualClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * The BoardController class manages the game board and player interactions.
 */
public class BoardController {
    /**
     * The current state of the game.
     */
    private GameState gameState;
    /**
     * Default BoardController constructor.
     */
    public BoardController () {
        try {
            List<String> disconnectingPlayers = Collections.synchronizedList(new LinkedList<>());
            this.gameState = new CreationState(new Board(), this, disconnectingPlayers);
        } catch (IllegalStateException ignore) {
            /*If an error occurs during first creation than the server will crash*/
        }
    }

    /**
     * Allows a player to join the game.
     * @param nickname the nickname of the player.
     * @param client the VirtualClient instance for the player.
     * @throws IllegalStateException if the game state does not allow joining.
     * @throws IllegalArgumentException if the nickname or client is invalid.
     */
    public synchronized void join(String nickname, VirtualClient client)
            throws IllegalStateException, IllegalArgumentException{
        gameState.join(nickname, client);
    }

    /**
     * Sets the number of players for the game.
     * @param nickname the nickname of the player setting the number.
     * @param num the number of players.
     * @throws IllegalStateException if the game state does not allow setting the number of players.
     * @throws IllegalArgumentException if the nickname or number of players is invalid.
     */
    public synchronized void setNumOfPlayers(String nickname, int num)
            throws IllegalStateException, IllegalArgumentException{
        gameState.setNumOfPlayers(nickname, num);
    }

    /**
     * Handles player disconnection.
     * @param nickname the nickname of the player.
     * @throws IllegalStateException if the game state does not allow disconnection.
     * @throws IllegalArgumentException if the nickname is invalid.
     */
    public void disconnect(String nickname)
            throws IllegalStateException, IllegalArgumentException{
        gameState.disconnectingPlayers.add(nickname);
        synchronized(this){
            gameState.disconnect(nickname);
        }
    }

    /**
     * Places the starting card for a player.
     * @param nickname the nickname of the player.
     * @param placeOnFront whether to place the card on the front.
     * @throws IllegalStateException if the game state does not allow placing the starting card.
     * @throws IllegalArgumentException if the nickname or placeOnFront is invalid.
     */
    public synchronized void placeStartingCard(String nickname, boolean placeOnFront)
            throws IllegalStateException, IllegalArgumentException{
        gameState.placeStartingCard(nickname, placeOnFront);
    }

    /**
     * Allows a player to choose their color.
     * @param nickname the nickname of the player.
     * @param color the chosen color.
     * @throws IllegalStateException if the game state does not allow choosing a color.
     * @throws IllegalArgumentException if the nickname or color is invalid.
     */
    public synchronized void chooseYourColor(String nickname, PlayerColor color)
            throws IllegalStateException, IllegalArgumentException{
        gameState.chooseYourColor(nickname, color);
    }

    /**
     * Allows a player to choose their secret objective.
     * @param nickname the nickname of the player.
     * @param choice the chosen secret objective.
     * @throws IllegalStateException if the game state does not allow choosing a secret objective.
     */
    public synchronized void chooseSecretObjective(String nickname, int choice)
            throws IllegalStateException{
        gameState.chooseSecretObjective(nickname, choice);
    }

    /**
     * Allows a player to draw a card from a deck.
     * @param nickname the nickname of the player.
     * @param deckFrom the deck to draw from.
     * @param cardPos the position of the card in the deck.
     * @throws IllegalStateException if the game state does not allow drawing.
     * @throws IllegalArgumentException if the nickname, deckFrom, or cardPos is invalid.
     */
    public synchronized void draw(String nickname, char deckFrom, int cardPos)
            throws IllegalStateException, IllegalArgumentException{
        gameState.draw(nickname, deckFrom, cardPos);
    }

    /**
     * Allows a player to place a card on the board.
     * @param nickname the nickname of the player.
     * @param cardID the ID of the card.
     * @param cardPos the position on the board to place the card.
     * @param cornerDir the direction of the card's corner.
     * @param placeOnFront whether to place the card on the front.
     * @throws IllegalStateException if the game state does not allow placing the card.
     * @throws IllegalArgumentException if the nickname, cardID, cardPos, cornerDir, or placeOnFront is invalid.
     */
    public synchronized void placeCard(String nickname, String cardID, GamePoint cardPos,
                                       CornerDirection cornerDir, boolean placeOnFront) throws IllegalStateException, IllegalArgumentException{
        gameState.placeCard(nickname, cardID, cardPos, cornerDir, placeOnFront);
    }

    /**
     * Restarts the game with a specified number of players.
     * @param nickname the nickname of the player initiating the restart.
     * @param numOfPlayers the number of players for the new game.
     * @throws IllegalStateException if the game state does not allow restarting the game.
     */
    public synchronized void restartGame(String nickname, int numOfPlayers) throws IllegalStateException{
            gameState.restartGame(nickname, numOfPlayers);
    }

    /**
     * Gets the current game state.
     * @return the current game state.
     */
    synchronized GameState getGameState(){
        return gameState;
    }

    /**
     * Sets the next game state.
     * @param nextState the next game state.
     */
    synchronized void setGameState(GameState nextState){
        gameState = nextState;
    }
}
