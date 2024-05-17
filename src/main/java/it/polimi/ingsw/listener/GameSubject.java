package it.polimi.ingsw.listener;

import it.polimi.ingsw.listener.events.GameEvent;

import java.rmi.RemoteException;

public interface GameSubject {
    void addListener(String nickname, GameListener client);
    void removeListener(String nickname);
    void notifyAllListener(GameEvent event) throws RemoteException;
    void notifyListener(String nickname, GameEvent event) throws RemoteException;
}
