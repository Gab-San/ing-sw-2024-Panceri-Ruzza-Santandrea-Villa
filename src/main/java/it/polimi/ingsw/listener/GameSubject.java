package it.polimi.ingsw.listener;

import it.polimi.ingsw.model.exceptions.ListenException;

import java.rmi.RemoteException;

public interface GameSubject {
    void addListener(GameListener listener);
    void removeListener(GameListener listener);
    void notifyAllListeners(GameEvent event);
    void notifyListener(GameListener listener, GameEvent event) throws ListenException;
}
