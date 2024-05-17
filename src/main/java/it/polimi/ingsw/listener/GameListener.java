package it.polimi.ingsw.listener;

import it.polimi.ingsw.model.exceptions.ListenException;

import java.rmi.RemoteException;

public interface GameListener {
    void listen(GameEvent event) throws ListenException;
}
