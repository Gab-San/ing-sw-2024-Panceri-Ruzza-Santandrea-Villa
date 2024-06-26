package it.polimi.ingsw.network;

import it.polimi.ingsw.GamePoint;

import java.rmi.Remote;
import java.rmi.RemoteException;


/**
 * This interface represents the skeletal structure of a proxy client-side. It is used to
 * further hide the network interface as functions arguments' type are more similar to the model.
 */
public interface CommandPassthrough extends Remote{

    /**
     * Sends a message to the specified addressee.
     *
     * <p>
     *     Takes "all" as a parameter that broadcasts the message to
     *     every connected player.
     * </p>
     *
     * @param addressee nickname of the message receiver
     * @param message text to be sent
     * @throws RemoteException if an error occurs during connection
     */
    void sendMsg(String addressee, String message) throws RemoteException;
    /**
     * Subscribes the player to the server, putting them in a lobby if possible.
     * <p>
     *     Nicknames are unique and bind the client to the server. Therefore if a player
     *     is subject to an abnormal disconnection he must reconnect with the same nickname in
     *     order to reconnect.
     * </p>
     * @param nickname user selected identifier
     * @throws IllegalStateException may be thrown for different reasons: <br>
     * - the client is already connected; <br>
     * - the nickname passed is already in use; <br>
     * - the game reaches an illegal state of execution.
     * @throws RemoteException if a connection error occurs
     */
    void connect(String nickname) throws IllegalStateException, RemoteException;

    /**
     * Sets the number of player required to play the match.
     *
     * @param num number of player to wait before starting the match
     * @throws RemoteException if a connection error occurs
     */
    void setNumOfPlayers(int num) throws RemoteException;

    /**
     * Unsubscribes the player from the server.
     *
     * <p>
     *     The bind between the user and their identifier might or might not be
     *     released, depending on the stage of the match in which the user left.
     * </p>
     * @throws IllegalStateException may be thrown for different reasons: <br>
     * - nickname doesn't match any of the connected players' nicknames <br>
     * - a client instance not connected is trying to disconnect <br>
     * - game reaches an illegal state of execution.
     * @throws IllegalArgumentException may be thrown for different reasons: <br>
     * - nickname doesn't match any of the connected players' nicknames <br>
     * - an inner state exception
     * @throws RemoteException if a connection error occurs
     */
    void disconnect() throws IllegalStateException, IllegalArgumentException, RemoteException;
    /**
     * Places the starting card.
     *
     * @param placeOnFront true if the face of the card is up, false if down
     * @throws RemoteException if an error occurs during connection
     */
    void placeStartCard(boolean placeOnFront) throws RemoteException;
    /**
     * Chooses the player's colour.
     *
     * @param colour chosen colour
     * @throws RemoteException if an error occurs during connection
     */
    void chooseColor(char colour) throws RemoteException;

    /**
     * Chooses the player secret objective between the available options.
     *
     * @param choice index of the card to keep
     * @throws RemoteException if an error occurs during connection
     */
    void chooseObjective(int choice) throws RemoteException;
    /**
     * Places the chosen card on the player's play area.
     *
     * <p>
     *     The chosen card is placed on the corner selected by the user.
     *     The corner is represented with the point of the placed card containing
     *     the corner and its direction.
     * </p>
     *
     * @param cardID id of the card to be placed
     * @param placePos position of the card containing the selected corner
     * @param cornerDir direction of the selected corner
     * @param placeOnFront true if the face of the card is up, false if down
     * @throws RemoteException if an error occurs during connection
     */
    void placeCard(String cardID, GamePoint placePos, String cornerDir, boolean placeOnFront) throws RemoteException;

    /**
     * Draw the selected cardPosition on the player's play area.
     *
     * @param deck identifier of the deck
     * @param cardPosition identifier of the cardPosition position
     * @throws RemoteException if an error occurs during connection
     */
    void draw(char deck, int cardPosition) throws RemoteException;

    /**
     * Restarts the game with the given number of player
     *
     * @param numOfPlayers number of player to wait before starting the match: if lower than the connected players at the time of the call
     *                     the server will include all the connected players.
     * @throws RemoteException if an error occurs during connection
     */
    void restartGame(int numOfPlayers) throws RemoteException;

    /**
     * Pings the remote end of the connection, testing if reachable
     * @throws RemoteException if an error occurs during connection
     */
    void ping() throws RemoteException;

}
