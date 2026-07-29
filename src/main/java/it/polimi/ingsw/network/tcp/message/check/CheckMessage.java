package it.polimi.ingsw.network.tcp.message.check;

import it.polimi.ingsw.network.tcp.client.TCPServerProxy;
import it.polimi.ingsw.network.tcp.message.TCPServerCheckMessage;

import java.io.Serial;

/**
 * This class implements the tcp server check interface.
 * <p>
 *     Check messages are used to make tcp calls synchronous.
 * </p>
 */
public class CheckMessage implements TCPServerCheckMessage {
    @Serial
    private static final long serialVersionUID = 168L;
    private final IllegalStateException stateException;
    private final IllegalArgumentException argumentException;

    /**
     * Default constructor for check message.
     * <p>
     *     This constructor is used if no error occurred
     *     while executing
     * </p>
     */
    public CheckMessage(){
        stateException = null;
        argumentException = null;
    }

    /**
     * Constructs a check message that carries an illegal state exception.
     * @param stateException illegal exception raised while executing
     */
    public CheckMessage(IllegalStateException stateException){
        this.stateException = stateException;
        argumentException = null;
    }

    /**
     * Constructs a check message that carries an illegal argument exception.
     * @param argumentException illegal argument exception raised while executing
     */
    public CheckMessage(IllegalArgumentException argumentException){
        this.argumentException = argumentException;
        stateException = null;
    }

    @Override
    public void handle(TCPServerProxy client) throws IllegalStateException, IllegalArgumentException{
        if(stateException != null){
            throw stateException;
        }

        if(argumentException != null){
            throw argumentException;
        }
    }

    @Override
    public boolean isCheck() {
        return true;
    }
}
