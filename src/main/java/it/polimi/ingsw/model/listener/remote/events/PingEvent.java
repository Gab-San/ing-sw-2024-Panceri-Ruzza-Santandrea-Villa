package it.polimi.ingsw.model.listener.remote.events;

import it.polimi.ingsw.server.VirtualClient;

import java.rmi.RemoteException;

public class PingEvent implements NetworkEvent {
    private final String nickname;
    public PingEvent(String nickname){
        this.nickname = nickname;
    }
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.ping();
    }

    public String getNickname(){
        return nickname;
    }
}
