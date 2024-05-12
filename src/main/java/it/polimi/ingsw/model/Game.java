package it.polimi.ingsw.model;

import it.polimi.ingsw.server.VirtualClient;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Game {
    private final String gameID;
    private final Set<VirtualClient> clientList;

    public Game(String gameID){
        this.gameID = gameID;
        clientList = new HashSet<>();
    }

    public void addClient(VirtualClient client){
        synchronized (clientList) {
            clientList.add(client);
        }
    }
    public void removeClient(VirtualClient client){
        synchronized (clientList) {
            clientList.remove(client);
        }
    }
    public String getGameID(){
        return gameID;
    }
    public void updateAll(){
        // could also synchronize on the single client? Probably not necessary though
        synchronized (clientList){
            //TODO: implement this with View updates
        }
    }
    public Set<VirtualClient> getClientList(){return Collections.unmodifiableSet(clientList);}
}
