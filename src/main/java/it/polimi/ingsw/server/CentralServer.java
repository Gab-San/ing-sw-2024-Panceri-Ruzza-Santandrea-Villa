package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.BoardController;
import it.polimi.ingsw.server.Commands.GameCommand;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

// FIXME: add VirtualClient to all function calls (to check that a client doesn't send commands for other players)?
public class CentralServer {
    private static CentralServer singleton;
    private final Map<String, VirtualClient> playerClients;   // key == player nickname
    private final BlockingQueue<GameCommand> commandQueue;
    private final BoardController gameRef;

    private CentralServer() throws IllegalStateException{
        playerClients = new Hashtable<>();
        commandQueue = new LinkedBlockingDeque<>();
        gameRef = new BoardController("");
        //TODO: review the update/command queue executors
        new Thread(getQueueExtractor(commandQueue)).start();
    }

    public synchronized static CentralServer getSingleton() throws IllegalStateException{
        if(singleton == null) singleton = new CentralServer();
        return singleton;
    }
    public void issueGameCommand(GameCommand action) throws InterruptedException {
        commandQueue.put(action);
    }
    public synchronized VirtualClient getClientFromNickname(String nickname){
        return playerClients.get(nickname);
    }
    public BoardController getGameRef() {
        return gameRef; // not synchronized as gameRef is final
    }

    //FIXME Work onto executor for game commands
    private Runnable getQueueExtractor(BlockingQueue<GameCommand> queue){
        return () -> {
            try {
                while(true) {
                    GameCommand command = queue.take();
                    command.execute();
                }
            }catch (InterruptedException ex){
                //TODO: handle exception?
            }
        };
    }

    // TODO: add custom exception for invalid connection? (duplicate nickname)
    public synchronized void connect(String nickname, VirtualClient client) throws IllegalStateException{
        //check unique nickname
        if(playerClients.containsValue(client))
            throw new IllegalStateException("Client already connected!");

        VirtualClient oldClient = null;
        if(playerClients.containsKey(nickname)){
            try{
                playerClients.get(nickname).ping();
                throw new IllegalStateException("Player with nickname "+nickname+" already connected!");
            }catch (RemoteException | ConnectionLostException clientLostConnection){
                oldClient = playerClients.put(nickname, client);
            }
            if(gameRef != null){ // if player is in game
                //TODO: send current state to player and
                // substitute new client to oldClient in all observers
            }
        }
        else{
            try{
                gameRef.join(nickname, client);
                playerClients.put(nickname, client);
                //TODO: subscribe client to all observers
            }catch (IllegalStateException e) {
                throw new IllegalStateException("Player can't connect to game\n" +
                    "Error message: " + e.getMessage());
            }
        }
    }

    public synchronized void disconnect(String nickname, VirtualClient client) throws IllegalStateException{
        if(!playerClients.containsKey(nickname)) throw new IllegalStateException("Player not connected!");
        if(!client.equals(playerClients.get(nickname))) throw new IllegalStateException("Illegal request, Client instance does not match!");

        gameRef.disconnect(nickname, client);
        //TODO: unsubscribe client from all observers
    }

}
