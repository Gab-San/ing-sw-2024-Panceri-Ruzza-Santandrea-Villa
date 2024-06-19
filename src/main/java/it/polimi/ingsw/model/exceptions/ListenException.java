package it.polimi.ingsw.model.exceptions;

/**
 * Signals that an error occurred while handling the event.
 */
public class ListenException extends RuntimeException{
    /**
     * Constructs a ListenException with no detail message.
     */
    public ListenException(){
        super();
    }
}
