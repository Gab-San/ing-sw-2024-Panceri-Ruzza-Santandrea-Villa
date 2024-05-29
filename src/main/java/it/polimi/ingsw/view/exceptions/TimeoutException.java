package it.polimi.ingsw.view.exceptions;

public class TimeoutException extends Exception{
    public TimeoutException(String msg) {
        super(msg);
    }
    public TimeoutException() {
        super("TIMEOUT");
    }
}
