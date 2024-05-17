package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.PlayerColor;
import it.polimi.ingsw.listener.GameEvent;
import it.polimi.ingsw.listener.GameListener;
import it.polimi.ingsw.listener.GameSubject;
import org.jetbrains.annotations.Range;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

public class Player implements GameSubject {
    private final String nickname;
    @Range(from=1,to=4)
    private int turn;
    private boolean isConnected;
    private final PlayerHand hand;
    private PlayerColor color;
    private final List<GameListener> gameListenerList;

    public Player(String nickname){
        this.nickname = nickname;
        isConnected = true;
        hand = new PlayerHand(this);
        gameListenerList = new LinkedList<>();
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
    public void addListener(GameListener listener) {
        gameListenerList.add(listener);
    }

    @Override
    public void removeListener(GameListener listener) {
        gameListenerList.remove(listener);
    }

    @Override
    public void notifyListeners(GameEvent event) throws RemoteException {
        for(GameListener listener: gameListenerList){
            listener.listen(event);
        }
    }
}
