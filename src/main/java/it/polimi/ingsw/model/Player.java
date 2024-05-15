package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.PlayerColor;
import org.jetbrains.annotations.Range;

public class Player {
    private final String nickname;
    @Range(from=1,to=4)
    private int turn;
    private boolean isConnected;
    private final PlayerHand hand;
    private PlayerColor color;

    public Player(String nickname){
        this.nickname = nickname;
        isConnected = true;
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

    public String getNickname(){
        return nickname;
    }
    public int getTurn(){
        return turn;
    }
    public void setTurn(int turn) {
        this.turn = turn;
    }
    public boolean isConnected() {
        return isConnected;
    }
    public void setConnected(boolean connected) {
        isConnected = connected;
    }
    public PlayerHand getHand() {
        return hand;
    }
    public PlayerColor getColor() {
        return color;
    }
    public void setColor(PlayerColor newColor){ this.color=newColor;}

}
