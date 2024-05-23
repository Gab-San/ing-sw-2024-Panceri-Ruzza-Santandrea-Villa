package it.polimi.ingsw.model.listener.remote.errors;

import it.polimi.ingsw.model.listener.remote.NetworkEvent;

abstract public class RemoteErrorEvent implements NetworkEvent {
    protected final String notifiedClient;
    protected RemoteErrorEvent(String notifiedClient){

        this.notifiedClient = notifiedClient;
    }
    public String getUser(){
        return notifiedClient;
    }
}
