package it.polimi.ingsw.model.exceptions;

public class DeckException extends Exception{

    private Class<?> factoryType;
    public DeckException(){
        super();
    }
    public DeckException(String message){
        super(message);
    }

    public DeckException(String message, Throwable cause){
        super(message, cause);
    }
    public DeckException(Throwable cause){
        super(cause);
    }

    public DeckException(Class<?> cardFactoryType){
        this.factoryType = cardFactoryType;
    }
    public DeckException(String message, Class<?> cardFactoryType){
        super(message);
        this.factoryType = cardFactoryType;
    }

    public DeckException(Throwable cause, Class<?> cardFactoryType){
        this(cause);
        this.factoryType = cardFactoryType;
    }

    public DeckException(String message, Throwable cause, Class<?> cardFactoryType){
        this(message, cause);
        this.factoryType = cardFactoryType;
    }

    public Class<?> getDeck(){
        return factoryType;
    }

    @Override
    public String toString() {
        return super.toString() + " factoryType=" + factoryType;
    }
}
