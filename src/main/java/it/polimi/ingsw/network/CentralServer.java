package it.polimi.ingsw.network;

import com.diogonunes.jcolor.Attribute;
import it.polimi.ingsw.controller.BoardController;
import it.polimi.ingsw.model.exceptions.DeckException;
import it.polimi.ingsw.network.Commands.GameCommand;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;

import static com.diogonunes.jcolor.Ansi.colorize;

public class CentralServer {
    private static CentralServer singleton;
    private final Map<String, VirtualClient> playerClients;   // key == player nickname
    private final Queue<GameCommand> commandQueue;
    private final BoardController gameRef;

    private CentralServer() throws IllegalStateException{
        playerClients = new Hashtable<>();
        commandQueue = new LinkedBlockingDeque<>();
        gameRef = new BoardController();
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

    public synchronized void connect(String nickname, VirtualClient client) throws IllegalStateException {
        if(playerClients.containsValue(client))
            throw new IllegalStateException("Client already connected!");

        if(playerClients.containsKey(nickname)){
            try{
                playerClients.get(nickname).ping();
                throw new IllegalStateException("Player with nickname "+nickname+" already connected!");
            }catch (RemoteException clientLostConnection){
                playerClients.put(nickname, client);
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
                try {
                    gameRef.join(nickname, client);
                } catch (IllegalArgumentException exception){
                    throw new IllegalStateException(exception.getMessage());
                }
                playerClients.put(nickname, client);
            }catch (IllegalStateException e) {
                throw new IllegalStateException("Player can't connect to game\n" +
                    "Error message: " + e.getMessage());
            }
        }
    }

    public synchronized void disconnect(String nickname, VirtualClient client) throws IllegalStateException, IllegalArgumentException{
        if(!playerClients.containsKey(nickname)) throw new IllegalStateException("Player not connected!");
        if(!client.equals(playerClients.get(nickname))) throw new IllegalStateException("Illegal request, Client instance does not match!");

        gameRef.disconnect(nickname);
        playerClients.remove(nickname);
        synchronized (commandQueue) {
            commandQueue.removeAll(
                    commandQueue.stream().filter(c -> c.getNickname().equals(nickname)).toList()
            );
        }
    }

    public synchronized void updateMsg(String fullMessage) {
        List<String> disconnectedClients = new ArrayList<>();
        for(String nickname : playerClients.keySet()){
            try{
                playerClients.get(nickname).update(colorize(fullMessage, Attribute.GREEN_TEXT()));
            }catch (RemoteException e) {
                disconnectedClients.add(nickname);
            }
        }
        disconnectConnectionLossClients(disconnectedClients);
    }

    private void disconnectConnectionLossClients(List<String> disconnectedClients) {
        for(String nickname: disconnectedClients){
            disconnect(nickname, playerClients.get(nickname));
        }
        for(String nickname: disconnectedClients){
            updateMsg("Disconnected " + nickname + " for connection loss");
        }
    }
}
