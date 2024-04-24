package it.polimi.ingsw.model;

import java.util.HashSet;
import java.util.Set;

//TODO: delete this after creating the actual VirtualClient
class VirtualClient{}

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

}
