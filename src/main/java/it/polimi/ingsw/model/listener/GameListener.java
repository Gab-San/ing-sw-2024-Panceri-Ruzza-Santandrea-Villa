package it.polimi.ingsw.model.listener;

import it.polimi.ingsw.model.exceptions.ListenException;

/**
 * This interface represents a listener in the observer pattern.
 */
public interface GameListener {
    /**
     * Listens and handles the event.
     * @param event event triggered by a modification of the subject
     * @throws ListenException if an error occurs during event handling
     */
    void listen(GameEvent event) throws ListenException;
}
