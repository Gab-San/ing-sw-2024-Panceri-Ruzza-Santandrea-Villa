package it.polimi.ingsw.listener;

import java.rmi.RemoteException;

public interface GameSubject {
    void addListener(GameListener listener);
    void removeListener(GameListener listener);
    void notifyListeners(GameEvent event) throws RemoteException;
}
