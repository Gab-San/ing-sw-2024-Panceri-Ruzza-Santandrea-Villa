package it.polimi.ingsw.model;

import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.model.exceptions.ListenException;
import it.polimi.ingsw.model.listener.GameEvent;
import it.polimi.ingsw.model.listener.GameListener;
import it.polimi.ingsw.model.listener.GameSubject;
import it.polimi.ingsw.model.listener.remote.events.player.SetColorEvent;
import it.polimi.ingsw.model.listener.remote.events.player.SetConnectEvent;
import it.polimi.ingsw.model.listener.remote.events.player.SetTurnEvent;
import org.jetbrains.annotations.Range;

import java.util.LinkedList;
import java.util.List;

//DOCS complete docs
public class Player implements GameSubject {
    private final String nickname;
    @Range(from=1,to=4)
    private int turn;
    private boolean isConnected;
    private final PlayerHand hand;
    private PlayerColor color;
    private final List<GameListener> gameListenerList;

    public Player(String nickname){
        gameListenerList = new LinkedList<>();
        this.nickname = nickname;
        isConnected = true;
        turn = 0;
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
        notifyAllListeners(new SetTurnEvent(nickname, turn));
    }
    public boolean isConnected() {
        return isConnected;
    }
    public void setConnected(boolean connected) {
        isConnected = connected;
        notifyAllListeners(new SetConnectEvent(nickname, connected));
    }
    public PlayerHand getHand() {
        return hand;
    }
    public PlayerColor getColor() {
        return color;
    }
    public void setColor(PlayerColor newColor){
        this.color=newColor;
        notifyAllListeners(new SetColorEvent(nickname, newColor));
    }


    @Override
    public void addListener(GameListener listener) {
        synchronized (gameListenerList) {
            gameListenerList.add(listener);
        }
    }

    @Override
    public void removeListener(GameListener listener) {
        synchronized (gameListenerList) {
            gameListenerList.remove(listener);
        }
    }

    @Override
    public void notifyAllListeners(GameEvent event) throws ListenException {
        synchronized (gameListenerList) {
            for (GameListener listener : gameListenerList) {
                listener.listen(event);
            }
        }
    }
}
