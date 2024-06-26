package it.polimi.ingsw.model.listener.remote.errors;

import it.polimi.ingsw.model.listener.remote.NetworkEvent;

/**
 * This class represents a remote error event. An abstraction of a network event that is to be reported once
 * and doesn't need further handling.
 */
abstract public class RemoteErrorEvent implements NetworkEvent {
    /**
     * Associated user identifier.
     */
    protected final String notifiedClient;

    /**
     * Constructs an error event for the specified user.
     * @param notifiedClient user identifier
     */
    protected RemoteErrorEvent(String notifiedClient){

        this.notifiedClient = notifiedClient;
    }

    /**
     * Returns the user id associated with the client to be notified.
     * @return user id
     */
    public String getUser(){
        return notifiedClient;
    }
}
