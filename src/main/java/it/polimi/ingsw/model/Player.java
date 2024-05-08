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
        this(nickname, null, 0);
    }
    public Player(String nickname, PlayerColor color, int turn){
        this.nickname = nickname;
        this.turn = turn;
        isConnected = true;
        hand = new PlayerHand(this);
        this.color = color;
    }

    @Override
    public boolean equals(Object other) {
        if(other == this) return true;
        if(other instanceof Player player){
            return nickname.equals(player.nickname); // &&
                    //FIXME: compare other attributes?? Nickname is unique so it should be enough
            /*        isConnected == player.isConnected &&
                    turn == player.turn &&
                    hand.equals(player.hand) &&
                    color.equals(player.color);       */
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
