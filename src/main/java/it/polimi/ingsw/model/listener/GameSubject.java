package it.polimi.ingsw.model.listener;

import it.polimi.ingsw.model.exceptions.ListenException;

/**
 * This interface represents a subject of an observer pattern.
 */
public interface GameSubject {
    /**
     * Adds a listener of this subject.
     * @param listener listener to add
     */
    void addListener(GameListener listener);

    /**
     * Removes a listener of this subject.
     * @param listener listener to remove
     */
    void removeListener(GameListener listener);

    /**
     * Notifies all listeners of a change in the subject.
     * @param event event triggered by a modification of the subject
     * @throws ListenException if a error occurs while notifying a listener
     */
    void notifyAllListeners(GameEvent event) throws ListenException;
}
