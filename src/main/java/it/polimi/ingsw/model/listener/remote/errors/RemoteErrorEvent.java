package it.polimi.ingsw.model.listener.remote.errors;

import it.polimi.ingsw.model.listener.remote.NetworkEvent;

abstract public class RemoteErrorEvent implements NetworkEvent {
    protected final String userNickname;
    protected RemoteErrorEvent(String userNickname){

        this.userNickname = userNickname;
    }
    public String getUser(){
        return userNickname;
    };
}
