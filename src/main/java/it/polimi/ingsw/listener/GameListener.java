package it.polimi.ingsw.listener;

import java.rmi.RemoteException;

public interface GameListener {
    void listen(GameEvent event) throws RemoteException;
}
