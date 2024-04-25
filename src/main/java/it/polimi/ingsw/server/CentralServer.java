package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.BoardController;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class CentralServer {
    private final Map<String, VirtualClient> playerClients;   // key == player nickname
    private final Set<String> playersInLobby;                 // list of player nicknames
    private final Map<String, BoardController> playerGames;   // key == player nickname
    private final Map<String, BoardController> gamesByID;     // key == game ID
    private final BlockingQueue<ClientUpdateCommand> lobbyUpdatesQueue;
    private final BlockingQueue<ClientUpdateCommand> gameUpdatesQueue;
    private final BlockingQueue<GameActionCommand> actionQueue;

    public Map<String, VirtualClient> getPlayerClients() {
        return playerClients;
    }
    public Set<String> getPlayersInLobby() {
        return playersInLobby;
    }
    public Map<String, BoardController> getPlayerGames() {
        return playerGames;
    }
    public Map<String, BoardController> getGamesByID() {
        return gamesByID;
    }

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

        //TODO: review the update/command queue executors
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

    // TODO: add custom exception for invalid connection (duplicate nickname)
    public synchronized void connect(String nickname, VirtualClient client) throws RuntimeException{
        //check unique nickname
        if(playerClients.containsKey(nickname)){
            throw new RuntimeException("Nickname already taken!");
        }
        // else
        VirtualClient oldClient = playerClients.put(nickname, client);

        BoardController game = playerGames.get(nickname);
        if(game != null){
            game.replaceClient(nickname, oldClient, client);
        } else {
            playersInLobby.add(nickname); //leaves playersInLobby unchanged if reconnecting
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
