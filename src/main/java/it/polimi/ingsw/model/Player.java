package it.polimi.ingsw.model;

import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.model.exceptions.ListenException;
import it.polimi.ingsw.model.listener.GameEvent;
import it.polimi.ingsw.model.listener.GameListener;
import it.polimi.ingsw.model.listener.GameSubject;
import it.polimi.ingsw.model.listener.remote.events.player.SetColorEvent;
import it.polimi.ingsw.model.listener.remote.events.player.SetConnectEvent;
import it.polimi.ingsw.model.listener.remote.events.player.SetTurnEvent;
import org.jetbrains.annotations.Range;

import java.util.LinkedList;
import java.util.List;

/**
 * This class represents a player. A player is an entity that sits to play the game
 * and is characterized by the following attributes: <br>
 * - is active; <br>
 * - has a turn assigned in which to play; <br>
 * - can choose a color; <br>
 * - is identifier by a unique string named nickname.
 */
public class Player implements GameSubject {
    private final String nickname;
    @Range(from=1,to=4)
    private int turn;
    private boolean isConnected;
    private final PlayerHand hand;
    private PlayerColor color;
    private final List<GameListener> gameListenerList;

    /**
     * Initializes a player with the specified nickname.
     * @param nickname unique identifier of the player
     */
    public Player(String nickname){
        gameListenerList = new LinkedList<>();
        this.nickname = nickname;
        isConnected = true;
        turn = 0;
        color = null;
        hand = new PlayerHand(this);
    }

    @Override
    public boolean equals(Object other) {
        if(other == this) return true;
        if(other instanceof Player player){
            return nickname.equals(player.nickname);
        }
        else return false;
    }
    @Override
    public int hashCode(){
        return nickname.hashCode();
    }

    /**
     * Returns player's nickname. It is their unique identifier.
     * @return player's nickname
     */
    public String getNickname(){
        return nickname;
    }

    /**
     * Returns the play turn assigned to the player.
     * @return player turn
     */
    public int getTurn(){
        return turn;
    }

    /**
     * Sets the player's turn.
     * @param turn turn in which the selected player is to play
     */
    public void setTurn(int turn) {
        this.turn = turn;
        notifyAllListeners(new SetTurnEvent(nickname, turn));
    }

    /**
     * Returns true if the player is connected, false otherwise.
     * @return true if the player is connected, false otherwise
     */
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * Sets player connection status. This represents the active status of a player.
     * @param connected true if the player is connected, false otherwise
     */
    public void setConnected(boolean connected) {
        isConnected = connected;
        notifyAllListeners(new SetConnectEvent(nickname, connected));
    }

    /**
     * Returns player's hand.
     * @return player's hand
     */
    public PlayerHand getHand() {
        return hand;
    }

    /**
     * Returns player's chosen color.
     * @return player's color
     */
    public PlayerColor getColor() {
        return color;
    }

    /**
     * Sets the player's color as chosen during choose color phase.
     * @param newColor chosen color
     */
    public void setColor(PlayerColor newColor){
        this.color=newColor;
        notifyAllListeners(new SetColorEvent(nickname, newColor));
    }


    @Override
    public void addListener(GameListener listener) {
        synchronized (gameListenerList) {
            gameListenerList.add(listener);
        }
    }

    @Override
    public void removeListener(GameListener listener) {
        synchronized (gameListenerList) {
            gameListenerList.remove(listener);
        }
    }

    @Override
    public void notifyAllListeners(GameEvent event) throws ListenException {
        synchronized (gameListenerList) {
            for (GameListener listener : gameListenerList) {
                listener.listen(event);
            }
        }
    }
}
