package it.polimi.ingsw.model.exceptions;

public class DeckInstantiationException extends ReflectiveOperationException {

    private Class<?> factoryType;

    public DeckInstantiationException(){
        super();
    }

    public DeckInstantiationException(String msg){
        super(msg);
    }

    public DeckInstantiationException(String msg, Throwable cause, Class<?> factoryType ){
        super(msg, cause);
        this.factoryType = factoryType;
    }

    public DeckInstantiationException(String msg, Class<?> factoryType){
        super(msg);
        this.factoryType = factoryType;
    }

    public Class<?> getDeck(){
        return factoryType;
    }
}
