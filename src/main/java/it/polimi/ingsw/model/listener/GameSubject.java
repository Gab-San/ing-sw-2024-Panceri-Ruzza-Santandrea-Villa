package it.polimi.ingsw.model.listener;

import it.polimi.ingsw.model.exceptions.ListenException;

public interface GameSubject {
    void addListener(GameListener listener);
    void removeListener(GameListener listener);
    void notifyAllListeners(GameEvent event);
    void notifyListener(GameListener listener, GameEvent event) throws ListenException;
}
