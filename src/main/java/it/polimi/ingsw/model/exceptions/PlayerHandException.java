package it.polimi.ingsw.model.exceptions;

import it.polimi.ingsw.model.Player;

public class PlayerHandException extends PlayerException{
    private final Class<?> cardClass;

    public PlayerHandException(String msg, Player player, Class<?> cardClass){
        super(msg, player);
        this.cardClass = cardClass;
    }
    
    public PlayerHandException(String msg, Player player){
        this(msg,player,null);
    }

    @Override
    public String toString() {
        return super.toString() + "cardClass= " + cardClass;
    }
}
