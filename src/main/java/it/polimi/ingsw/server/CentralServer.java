package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.BoardController;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;

// TODO: implement command pattern
//Commands should have a reference to CentralServer in order to get games or handle the disconnection (or connection loss)
abstract class ClientUpdateCommand implements Runnable {
    @Override
    public abstract void run();
}
abstract class GameActionCommand implements Runnable{
    @Override
    public abstract void run();
}

public class CentralServer {
    private Map<String, VirtualClient> playerClients;   // key == player nickname
    private Set<String> playersInLobby;                 // list of player nicknames
    private Map<String, BoardController> playerGames;   // key == player nickname
    private Map<String, BoardController> gamesByID;     // key == game ID
    private BlockingQueue<ClientUpdateCommand> lobbyUpdatesQueue;
    private BlockingQueue<ClientUpdateCommand> gameUpdatesQueue;
    private BlockingQueue<GameActionCommand> actionQueue;

    private Runnable getQueueExtractor(BlockingQueue<? extends Runnable> queue){
        return () -> {
            try {
                while(true) {
                    Runnable command = queue.take();
                    command.run();
                }
            }catch (InterruptedException ex){
                //TODO: handle exception?
            }
        };
    }
    CentralServer(){
        playerClients = new Hashtable<>();
        playersInLobby = new HashSet<>();
        playerGames = new Hashtable<>();
        gamesByID = new Hashtable<>();

        lobbyUpdatesQueue = new LinkedBlockingDeque<>();
        gameUpdatesQueue = new LinkedBlockingDeque<>();
        actionQueue = new LinkedBlockingDeque<>();

        new Thread(getQueueExtractor(lobbyUpdatesQueue)).start();
        new Thread(getQueueExtractor(gameUpdatesQueue)).start();
        new Thread(getQueueExtractor(actionQueue)).start();
    }

    private synchronized BoardController getGame(String gameID){
        if(gamesByID.containsKey(gameID))
            return gamesByID.get(gameID);
        // else
        BoardController game = new BoardController(gameID);
        gamesByID.put(gameID, game);
        return game;
    }

    private synchronized void destroyGameIfEmpty(String gameID){
        BoardController game = gamesByID.get(gameID);
        if(game == null) return; // maybe handle error?? this would be impossible
        if(!playerGames.containsValue(game)){
            gamesByID.remove(gameID);
        }
    }

    // TODO: add exception for invalid connection (duplicate nickname)
    public synchronized void connect(String nickname, VirtualClient client) throws RuntimeException{
        //check unique nickname
        if(playerClients.containsKey(nickname)){
            throw new RuntimeException("Nickname already taken!");
        }
        // else
        playerClients.put(nickname, client);

        BoardController game = playerGames.get(nickname);
        if(game != null){
            //TODO: handle reconnection
        } else {
            playersInLobby.add(nickname);
        }
    }

    public void issueLobbyUpdate(ClientUpdateCommand update) throws InterruptedException {
        lobbyUpdatesQueue.put(update);
    }
    public void issueGameUpdate(ClientUpdateCommand update) throws InterruptedException {
        gameUpdatesQueue.put(update);
    }
    public void issueGameAction(GameActionCommand action) throws InterruptedException {
        actionQueue.put(action);
    }
}
