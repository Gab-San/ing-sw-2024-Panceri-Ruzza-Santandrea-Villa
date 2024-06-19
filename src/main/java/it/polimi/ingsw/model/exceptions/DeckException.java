package it.polimi.ingsw.model.exceptions;

/**
 * Signals that a request cannot be fulfilled by the specified deck.
 * <p>
 *     For example, trying to draw from an empty deck or if a requested card isn't part of the deck.
 * </p>
 */
public class DeckException extends Exception{

    private Class<?> factoryType;

    /**
     * Constructs a DeckException with no detail message.
     */
    public DeckException(){
        super();
    }

    /**
     * Constructs a DeckException with the specified detail message and cause.
     * @param message the detail message
     * @param cause the cause (a null value is permitted, and indicates that the cause is nonexistent or unknown)
     */
    public DeckException(String message, Throwable cause){
        super(message, cause);
    }

    /**
     * Constructs a DeckException with the specified detail message and the deck type .
     * that triggered the exception
     * @param message the detail message
     * @param cardFactoryType deck type
     */
    public DeckException(String message, Class<?> cardFactoryType){
        super(message);
        this.factoryType = cardFactoryType;
    }

    /**
     * Constructs a DeckException with the specified detail message and cause and deck type.
     * @param message the detail message
     * @param cause the cause (a null value is permitted, and indicates that the cause is nonexistent or unknown)
     * @param cardFactoryType deck type
     */
    public DeckException(String message, Throwable cause, Class<?> cardFactoryType){
        this(message, cause);
        this.factoryType = cardFactoryType;
    }
    

    @Override
    public String toString() {
        return super.toString() + " factoryType=" + factoryType;
    }
}
