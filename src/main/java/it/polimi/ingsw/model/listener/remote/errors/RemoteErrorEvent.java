package it.polimi.ingsw.model.listener.remote.errors;

import it.polimi.ingsw.model.listener.remote.NetworkEvent;

/**
 * This class represents a remote error event. An abstraction of a network event that is to be reported once
 * and doesn't need further handling.
 */
abstract public class RemoteErrorEvent implements NetworkEvent {
    protected final String notifiedClient;
    protected RemoteErrorEvent(String notifiedClient){

        this.notifiedClient = notifiedClient;
    }
    public String getUser(){
        return notifiedClient;
    }
}
