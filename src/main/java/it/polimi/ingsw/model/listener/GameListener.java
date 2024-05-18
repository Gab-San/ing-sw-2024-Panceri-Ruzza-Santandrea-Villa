package it.polimi.ingsw.model.listener;

import it.polimi.ingsw.model.exceptions.ListenException;

public interface GameListener {
    void listen(GameEvent event) throws ListenException;
}
