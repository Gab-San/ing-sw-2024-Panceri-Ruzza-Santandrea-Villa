package it.polimi.ingsw.model.exceptions;

public class ListenException extends RuntimeException{
    public ListenException(String errorMessage){
        super(errorMessage);
    }

    public ListenException(String errorMessage, Throwable cause){
        super(errorMessage,cause);
    }
    public ListenException(Throwable cause){
        super(cause);
    };
}
