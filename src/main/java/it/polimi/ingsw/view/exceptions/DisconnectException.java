package it.polimi.ingsw.view.exceptions;

/**
 * View exception to be thrown on a disconnection explicitly called by the user
 */
public class DisconnectException extends Exception{
    public DisconnectException() {
        super("DISCONNECT");
    }
}
