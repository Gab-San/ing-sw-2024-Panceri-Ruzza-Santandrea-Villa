package it.polimi.ingsw.model;

import it.polimi.ingsw.server.VirtualClient;

import java.util.*;

// TODO: replace this class with observer pattern
public class Game {
    private final String gameID;
    private final Map<String, VirtualClient> clientList;

    public Game(String gameID){
        this.gameID = gameID;
        clientList = new Hashtable<>();
    }

    public void addClient(String nickname, VirtualClient client){
        synchronized (clientList) {
            clientList.put(nickname, client);
        }
    }
    public void removeClient(String nickname){
        synchronized (clientList) {
            clientList.remove(nickname);
        }
    }
    public String getGameID(){
        return gameID;
    }

    public Map<String, VirtualClient> getClientList(){return Collections.unmodifiableMap(clientList);}
}
