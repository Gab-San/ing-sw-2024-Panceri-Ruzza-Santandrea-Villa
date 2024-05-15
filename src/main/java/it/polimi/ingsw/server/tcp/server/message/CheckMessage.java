package it.polimi.ingsw.server.tcp.server.message;

import it.polimi.ingsw.server.tcp.client.ClientSideProxy;
import it.polimi.ingsw.server.tcp.message.TCPServerCheckMessage;

import java.io.Serial;

public class CheckMessage implements TCPServerCheckMessage {
    @Serial
    private static final long serialVersionUID = 168L;
    private final IllegalStateException stateException;
    private final IllegalArgumentException argumentException;
    public CheckMessage(){
        stateException = null;
        argumentException = null;
    }
    public CheckMessage(IllegalStateException stateException){
        this.stateException = stateException;
        argumentException = null;
    }

    public CheckMessage(IllegalArgumentException argumentException){
        this.argumentException = argumentException;
        stateException = null;
    }
    @Override
    public boolean isCheck() {
        return true;
    }

    @Override
    public void handle(ClientSideProxy client) throws IllegalStateException, IllegalArgumentException{
        if(stateException != null){
            throw stateException;
        }

        if(argumentException != null){
            throw argumentException;
        }
    }
}
