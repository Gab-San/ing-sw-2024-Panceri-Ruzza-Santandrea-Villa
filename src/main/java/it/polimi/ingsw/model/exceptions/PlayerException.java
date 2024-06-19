package it.polimi.ingsw.model.exceptions;

import it.polimi.ingsw.model.Player;

/**
 * Signals that an action involving the specified player couldn't be completed due to execution error.
 */
public class PlayerException extends RuntimeException{
    private final Player player;

    /**
     * Constructs a PlayerException with a detail message and the player instance
     * which caused the error to be signaled.
     * @param msg the detail message
     * @param player the player instance which caused the error
     */
    public PlayerException(String msg, Player player){
        super(msg);
        this.player = player;
    }

    @Override
    public String toString() {
        return super.toString() + "player= " + player;
    }
}
