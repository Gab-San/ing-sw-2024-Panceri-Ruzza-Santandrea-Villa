package it.polimi.ingsw.view.exceptions;

/**
 * View exception to be thrown after the client is notified by the server
 * of being forcefully disconnected due to a timeout
 */
public class TimeoutException extends Exception{
    /**
     * Constructs a TimeoutException with no detail message.
     */
    public TimeoutException() {
        super("TIMEOUT");
    }
}
