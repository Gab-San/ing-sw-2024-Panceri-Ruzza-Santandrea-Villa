package it.polimi.ingsw.model.exceptions;

import it.polimi.ingsw.model.Player;

public class PlayerException extends RuntimeException{
    private final Player player;
    public PlayerException(String msg, Player player){
        super(msg);
        this.player = player;
    }

    @Override
    public String toString() {
        return super.toString() + "player= " + player;
    }
}
