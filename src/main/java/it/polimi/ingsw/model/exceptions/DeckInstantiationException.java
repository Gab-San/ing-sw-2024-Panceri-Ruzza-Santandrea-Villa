package it.polimi.ingsw.model.exceptions;

public class DeckInstantiationException extends Exception {

    private Class<?> factoryType;
    // TODO: Delete exception
    // and substitute with System exit
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
