package it.polimi.ingsw.model.exceptions;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerHand;

public class PlayerException extends RuntimeException{
    private Player player;
    public PlayerException(String msg, Player player){
        super(msg);
        this.player = player;
    }
    public PlayerException(String msg, Throwable cause, Player player){
        super(msg, cause);
        this.player = player;
    }

    public PlayerException(Player player){
        super();
        this.player = player;
    }

}
