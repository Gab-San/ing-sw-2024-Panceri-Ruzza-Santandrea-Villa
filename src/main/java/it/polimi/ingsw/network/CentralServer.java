package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.BoardController;
import it.polimi.ingsw.model.exceptions.DeckException;
import it.polimi.ingsw.network.commands.GameCommand;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

import static it.polimi.ingsw.view.tui.ConsoleTextColors.*;

/**
 * This class is the main server that manages all the commands
 * issued by the clients.
 */
public class CentralServer {
    private static CentralServer singleton;
    private final Map<String, VirtualClient> playerClients;   // key == player nickname
    private final Queue<GameCommand> commandQueue;
    private final static Object DEBUG_LOCK = new Object();
    private static boolean pointsDebugMode;
    private static boolean resDebugMode;
    private static boolean emptyDeckMode;
    private final BoardController gameRef;
    private final ChatHandler chat;

    private CentralServer() throws IllegalStateException{
        playerClients = new Hashtable<>();
        commandQueue = new LinkedBlockingDeque<>();
        gameRef = new BoardController();
        chat = new ChatHandler(this);
        startCommandExecutor();
    }

    public synchronized static CentralServer getSingleton() throws IllegalStateException{
        if (singleton == null) singleton = new CentralServer();
        return singleton;
    }

    public void issueGameCommand(GameCommand action) {
        synchronized (commandQueue) {
            commandQueue.offer(action);
            commandQueue.notifyAll();
        }
    }
    public synchronized VirtualClient getClientFromNickname(String nickname){
        return playerClients.get(nickname);
    }
    public BoardController getGameRef() {
        return gameRef;
    }


    private void startCommandExecutor(){
        Thread commandExecutor = new Thread(
                () -> {
                        while (true) {
                            GameCommand command;
                            synchronized (commandQueue) {
                                while (commandQueue.isEmpty()) {
                                    try {
                                        commandQueue.wait();
                                    } catch (InterruptedException e) {
                                        //TODO Application quit?
                                        // or restart with executors
                                    }
                                }
                                command = commandQueue.remove();
                            }

                            try {
                                command.execute();
                            } catch (IllegalStateException e) {
                                System.err.println("IllegalStateException raised while executing a command.");
                                System.err.println(e.getMessage());
                            } catch (IllegalArgumentException e) {
                                System.err.println("IllegalArgumentException raised while executing a command.");
                                System.err.println(e.getMessage());
                            } catch (DeckException e) {
                                System.err.println("DeckException raised while executing a command");
                                System.err.println(e.getMessage());
                            }
                        }
                }
        );
        commandExecutor.setDaemon(true);
        commandExecutor.start();
    }

    /**
     * Subscribes the player to the server, putting them in a lobby if possible.
     *
     * <p>
     *     Binds the player to the given nickname, unique identifier, and
     *     assigns them to an open lobby or reconnects the player to the game
     *     they left either directly or indirectly.
     * </p>
     *
     * @param nickname unique identifier of the user
     * @param client instance of the virtual client
     * @throws IllegalStateException may be thrown for different reasons: <br>
     * - the client is already connected; <br>
     * - the nickname passed is already in use; <br>
     * - an inner state exception is raised.
     */
    public synchronized void connect(String nickname, VirtualClient client) throws IllegalStateException {
        if(playerClients.containsValue(client))
            throw new IllegalStateException("Client already connected!");

        if(playerClients.containsKey(nickname)){
            try{
                playerClients.get(nickname).ping();
                throw new IllegalStateException("Player with nickname "+nickname+" already connected!");
            }catch (RemoteException clientLostConnection){
                disconnect(nickname, playerClients.get(nickname));
                playerClients.put(nickname, client);
                chat.addClient(nickname, client);
            }
            // if client actually lost connection / disconnected
            if(gameRef != null){
                try {
                    gameRef.join(nickname, client);
                } catch (IllegalArgumentException exception){
                    throw new IllegalStateException(exception.getMessage());
                }
            }
        } else {
            try{
                gameRef.join(nickname, client);
                playerClients.put(nickname, client);
                chat.addClient(nickname, client);
            }catch (IllegalStateException | IllegalArgumentException e) {
                throw new IllegalStateException("Player can't connect to game\n" +
                    "Error message: " + e.getMessage());
            }
        }

        System.out.println(GREEN_TEXT + nickname + " has connected!" + RESET);
    }

    /**
     * Unsubscribes the player from the server.
     *
     * <p>
     *     The bind between the user and their identifier might or might not be
     *     released, depending on the stage of the match in which the user left.
     * </p>
     * @param nickname unique identifier of the user
     * @param client instance of the disconnecting client
     * @throws IllegalStateException may be thrown for different reasons: <br>
     * - a client instance not connected is trying to disconnect <br>
     * - an inner state exception
     * @throws IllegalArgumentException may be thrown for different reasons: <br>
     * - nickname doesn't match any of the connected players' nicknames <br>
     * - an inner state exception
     */
    public synchronized void disconnect(String nickname, VirtualClient client) throws IllegalStateException, IllegalArgumentException{
        if(!playerClients.containsKey(nickname)) throw new IllegalArgumentException("Player not connected!");
        if(!client.equals(playerClients.get(nickname))) throw new IllegalStateException("Illegal request, Client instance does not match!");

        gameRef.disconnect(nickname);
        playerClients.remove(nickname);
        chat.removeClient(nickname);
        synchronized (commandQueue) {
            commandQueue.removeAll(
                    commandQueue.stream().filter(c -> c.getNickname().equals(nickname)).toList()
            );
        }
        System.out.println(PURPLE_TEXT + nickname + " has disconnected!" + RESET);
    }

    public synchronized void sendMessage(String messenger, String addressee, String message) throws IllegalArgumentException {
        if(!playerClients.containsKey(messenger)){
            throw new IllegalArgumentException("Client not connected to chat!");
        }
        chat.addMessage(messenger, addressee, message);
    }

//region DEBUG MODES
    public static void setPointsMode(boolean on){
        synchronized (DEBUG_LOCK) {
            pointsDebugMode = on;
            System.err.println("Points mode: " + on);
        }
    }

    public static boolean isPointsDebugMode(){
        synchronized (DEBUG_LOCK){
            return pointsDebugMode;
        }
    }

    public static void setResourcesMode(boolean on){
        synchronized (DEBUG_LOCK){
            resDebugMode = on;
            System.err.println("Resource mode: " + on);
        }
    }

    public static boolean isResDebugMode(){
        synchronized (DEBUG_LOCK){
            return resDebugMode;
        }
    }

    public static void setEmptyDeckMode(boolean on){
        synchronized (DEBUG_LOCK){
            emptyDeckMode = on;
            System.err.println("Empty deck mode: " + on);
        }
    }

    public static boolean isEmptyDeckMode(){
        synchronized (DEBUG_LOCK){
            return emptyDeckMode;
        }
    }

    public static void setDebugMode(boolean on){
        synchronized (DEBUG_LOCK){
            setEmptyDeckMode(on);
            setPointsMode(on);
            setResourcesMode(on);
        }
    }

//endregion
}
