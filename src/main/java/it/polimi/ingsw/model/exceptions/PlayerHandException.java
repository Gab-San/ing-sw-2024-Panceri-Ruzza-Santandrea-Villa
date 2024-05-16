package it.polimi.ingsw.model.exceptions;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerHand;
import it.polimi.ingsw.model.cards.Card;

public class PlayerHandException extends PlayerException{
    private Class<?> cardClass;
    public PlayerHandException(Player player,Class<?> cardClass){
        super(player);
        this.cardClass = cardClass;
    }

    public PlayerHandException(String msg, Player player, Class<?> cardClass){
        super(msg, player);
        this.cardClass = cardClass;
    }
    
    public PlayerHandException(String msg, Player player){
        this(msg,player,null);
    }

    public PlayerHandException(String msg, Throwable cause, Player player, Class<?> cardClass){
        super(msg, cause, player);
        this.cardClass = cardClass;
    }

}
