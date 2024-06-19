package it.polimi.ingsw.model.exceptions;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.Card;

/**
 * Signals that the requested action on the player hand cannot be fulfilled.
 */
public class PlayerHandException extends PlayerException{
    private final Class<? extends Card> cardClass;

    /**
     * Constructs a PlayerHandException with detail message, player instance which caused the error and
     * the card class which is involved in the request.
     * @param msg the detail message
     * @param player the player instance which caused the error
     * @param cardClass card class involved in the request
     */
    public PlayerHandException(String msg, Player player, Class<? extends Card> cardClass){
        super(msg, player);
        this.cardClass = cardClass;
    }
    /**
     * Constructs a PlayerHandException with detail message and the player instance which caused the error.
     * @param msg the detail message
     * @param player the player instance which caused the error
     */
    public PlayerHandException(String msg, Player player){
        this(msg,player,null);
    }

    @Override
    public String toString() {
        return super.toString() + "cardClass= " + cardClass;
    }
}
