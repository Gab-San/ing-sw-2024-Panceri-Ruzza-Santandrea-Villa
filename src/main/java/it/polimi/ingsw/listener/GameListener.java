package it.polimi.ingsw.listener;

import it.polimi.ingsw.listener.events.GameEvent;

import java.rmi.RemoteException;

public interface GameListener {
    void listen(GameEvent event) throws RemoteException;
}
