package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.PlayerColor;
import it.polimi.ingsw.listener.events.GameEvent;
import it.polimi.ingsw.listener.GameListener;
import it.polimi.ingsw.listener.GameSubject;
import org.jetbrains.annotations.Range;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class Player implements GameSubject {
    private final String nickname;
    @Range(from=1,to=4)
    private int turn;
    private boolean isConnected;
    private final PlayerHand hand;
    private PlayerColor color;
    private final Map<String, GameListener> playerListeners;

    public Player(String nickname){
        this.nickname = nickname;
        isConnected = true;
        hand = new PlayerHand(this);
        playerListeners = new HashMap<>();
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

    @Override
    public void addListener(String nickname, GameListener client) {
        playerListeners.put(nickname, client);
    }

    @Override
    public void removeListener(String nickname) {
        playerListeners.remove(nickname);
    }

    @Override
    public void notifyAllListener(GameEvent event) throws RemoteException {
        for(String nickname: playerListeners.keySet()){
            playerListeners.get(nickname).listen(event);
        }
    }

    @Override
    public void notifyListener(String nickname, GameEvent event) throws RemoteException {
        playerListeners.get(nickname).listen(event);
    }

}
