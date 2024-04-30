package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.BoardController;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

// FIXME: add VirtualClient to all function calls (to check that a client doesn't send commands for other players)?
public class CentralServer {
    private static CentralServer singleton=null;
    private final Map<String, VirtualClient> playerClients;   // key == player nickname
    private final Set<String> playersInLobby;                 // list of player nicknames
    private final Map<String, BoardController> playerGames;   // key == player nickname
    private final Map<String, BoardController> gamesByID;     // key == game ID
    private final BlockingQueue<ClientUpdateCommand> lobbyUpdatesQueue;
    private final Map<BoardController, BlockingQueue<ClientUpdateCommand>> gameUpdatesQueues;
    private final Map<BoardController, BlockingQueue<GameActionCommand>> actionQueues;
    private final Map<BoardController, Thread[]> gameQueueThreads;

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
    public void issueLobbyUpdate(ClientUpdateCommand update) throws InterruptedException {
        lobbyUpdatesQueue.put(update);
    }
    public void issueGameUpdate(String gameID, ClientUpdateCommand update) throws InterruptedException {
        gameUpdatesQueues.get(gamesByID.get(gameID)).put(update);
    }
    public void issueGameAction(String gameID, GameActionCommand action) throws InterruptedException {
        actionQueues.get(gamesByID.get(gameID)).put(action);
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
                // legal situation: InterruptedException raised manually on game termination
            }
        };
    }
    public static CentralServer getSingleton() throws IllegalStateException{
        if(singleton == null) throw new IllegalStateException("CentralServer hasn't been initialized yet.");
        return singleton;
    }
    CentralServer() throws IllegalStateException{
        if(singleton != null) throw new IllegalStateException("Central Server can only have one instance!");
        else singleton = this;

        playerClients = new Hashtable<>();
        playersInLobby = new HashSet<>();
        playerGames = new Hashtable<>();
        gamesByID = new Hashtable<>();

        lobbyUpdatesQueue = new LinkedBlockingDeque<>();
        gameUpdatesQueues = new Hashtable<>();
        actionQueues = new Hashtable<>();
        gameQueueThreads = new Hashtable<>();

        //TODO: review the update/command queue executors
        new Thread(getQueueExtractor(lobbyUpdatesQueue)).start();
    }

    /**
     * Returns a game with the given gameID, creating a new game if it doesn't exist. <br>
     * Also instantiates all related queues and threads.
     * @param gameID the ID of the game to return
     * @return the BoardController linked to the game having gameID
     */
    private synchronized BoardController getGame(String gameID){
        if(gamesByID.containsKey(gameID))
            return gamesByID.get(gameID);

        // else create the game
        BoardController game = new BoardController(gameID);
        gamesByID.put(gameID, game);

        // create new game's actionQueue and updateQueue and associated threads
        gameUpdatesQueues.put(game, new LinkedBlockingDeque<>());
        actionQueues.put(game, new LinkedBlockingDeque<>());
        Thread t1 = new Thread( getQueueExtractor(gameUpdatesQueues.get(game)) );
        t1.start();
        Thread t2 = new Thread( getQueueExtractor(actionQueues.get(game)) );
        t2.start();
        // save thread references for game termination
        gameQueueThreads.put(game, new Thread[]{t1, t2});

        return game;
    }

    private synchronized void destroyGameIfEmpty(String gameID){
        BoardController game = gamesByID.get(gameID);
        if(game == null) return; // maybe handle error?? this would be impossible
        if(!playerGames.containsValue(game)){
            gamesByID.remove(gameID);
            Thread[] threads = gameQueueThreads.get(game);
            for(Thread t : threads){
                t.interrupt();
            }
            gameQueueThreads.remove(game);
            gameUpdatesQueues.remove(game);
            actionQueues.remove(game);
        }
    }

    // TODO: add custom exception for invalid connection? (duplicate nickname)
    public synchronized void connect(String nickname, VirtualClient client) throws IllegalStateException{
        //check unique nickname
        if(playerClients.containsValue(client))
            throw new IllegalStateException("Already connected!");

        VirtualClient oldClient = null;
        if(playerClients.containsKey(nickname)){
            try{
                playerClients.get(nickname).ping();
                throw new IllegalStateException("Player with nickname "+nickname+" already connected!");
            }catch (RemoteException | NullPointerException clientToBeReplaced){
                oldClient = playerClients.put(nickname, client);
            }

            BoardController game = playerGames.get(nickname);
            if(game != null){ // if player is in game
                game.replaceClient(nickname, oldClient, client);
            }
        }
        else{
            playerClients.put(nickname, client);
            playersInLobby.add(nickname);
        }
    }

    public synchronized void join(String nickname, String gameID) throws IllegalStateException{
        // check nickname connection
        if(!playerClients.containsKey(nickname)) throw new IllegalStateException("Player not connected!");
        if(playerGames.containsKey(nickname)) throw new IllegalStateException("Player already in game!");

        // creates game if it doesn't exist
        BoardController game = getGame(gameID);
        try {
            game.join(nickname, playerClients.get(nickname));
            playersInLobby.remove(nickname);
            playerGames.put(nickname, game);
        }catch (IllegalStateException e){
            throw new IllegalStateException("Player can't connect to game " + gameID + "\n" +
                    "Error message: " + e.getMessage());
        }
    }

    public synchronized void disconnect(String nickname, VirtualClient client) throws IllegalStateException{
        if(!playerClients.containsKey(nickname)) throw new IllegalStateException("Player not connected!");
        if(!playerClients.get(nickname).equals(client)) throw new IllegalStateException("Client instance does not match.");

        playersInLobby.remove(nickname); // unchanged if not in lobby
        if(playerGames.containsKey(nickname)){
            
        }
    }
}
