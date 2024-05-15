package it.polimi.ingsw.server;

import com.diogonunes.jcolor.Attribute;
import it.polimi.ingsw.controller.BoardController;
import it.polimi.ingsw.model.exceptions.DeckException;
import it.polimi.ingsw.server.Commands.GameCommand;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import static com.diogonunes.jcolor.Ansi.colorize;


import static com.diogonunes.jcolor.Ansi.colorize;

public class CentralServer {
    private static CentralServer singleton;
    private final Map<String, VirtualClient> playerClients;   // key == player nickname
    private final Queue<GameCommand> commandQueue;
    private final BoardController gameRef;

    private CentralServer() throws IllegalStateException{
        playerClients = new Hashtable<>();
        commandQueue = new LinkedBlockingDeque<>();
        gameRef = new BoardController("");
        startCommandExecutor();

    }

    public synchronized static CentralServer getSingleton() throws IllegalStateException{
        if (singleton == null) singleton = new CentralServer();
        return singleton;
    }

    public void issueGameCommand(GameCommand action) {
        commandQueue.offer(action);
    }
    public synchronized VirtualClient getClientFromNickname(String nickname){
        return playerClients.get(nickname);
    }
    public BoardController getGameRef() {
        return gameRef; // not synchronized as gameRef is final
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
                                        //TODO handle exception
                                        throw new RuntimeException(e);
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
            }catch (RemoteException clientLostConnection){
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

    public synchronized void disconnect(String nickname, VirtualClient client) throws IllegalStateException, IllegalArgumentException{
        //FIXME This check is useless: someone who is not connected is blocked by the client
        if(!playerClients.containsKey(nickname)) throw new IllegalStateException("Player not connected!");
        if(!client.equals(playerClients.get(nickname))) throw new IllegalStateException("Illegal request, Client instance does not match!");

        gameRef.disconnect(nickname);
        playerClients.remove(nickname);
        commandQueue.removeAll(
            commandQueue.stream().filter(c -> c.getNickname().equals(nickname)).toList()
        );
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
