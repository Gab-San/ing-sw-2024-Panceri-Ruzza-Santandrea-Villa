package it.polimi.ingsw.network;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class acts as a router for the messages sent from client to client.
 */
public class ChatHandler{

    private final Map<String, VirtualClient> connectedClients;
    private final Queue<DirectMessage> directMessageQueue;
    private final Queue<BroadcastMessage> broadcastMessageQueue;
    private boolean isOpen;
    private final CentralServer centralServer;
    private final List<Thread> threadPool;

    /**
     * Default constructor for chat handler.
     *
     * <p>
     *     It takes the central server at which all the messages arrive.
     * </p>
     * @param centralServer instance of the central server
     */
    public ChatHandler(CentralServer centralServer) {
        this.centralServer = centralServer;
        this.threadPool = new LinkedList<>();
        this.connectedClients = new Hashtable<>();
        directMessageQueue = new LinkedBlockingQueue<>();
        isOpen = true;
        broadcastMessageQueue = new LinkedBlockingQueue<>();
        startDirectMessageThread();
        startBroadcastMessageThread();
    }

    /**
     * Add a reachable client to the chat handler.
     * @param nickname player's unique nickname
     * @param client instance of virtual client bound to player
     */
    public void addClient(String nickname, VirtualClient client){
        synchronized (connectedClients) {
            connectedClients.put(nickname, client);
        }
        addMessage("SERVER", "ALL", nickname + " has connected!" );
    }

    /**
     * Remove a disconnecting client from chat handler.
     * @param nickname player's unique nickname
     */
    public void removeClient(String nickname){
        synchronized (connectedClients) {
            connectedClients.remove(nickname);
        }
        addMessage("SERVER", "all", "Disconnected " + nickname + "!");
    }

    /**
     * Add a message to the queue of messages to handle.
     * @param messenger messenger player's unique id
     * @param addressee addressee player's unique id
     * @param message message to send
     */
    public void addMessage(String messenger, String addressee, String message){
        if (addressee.equalsIgnoreCase("all")) {
            BroadcastMessage broadcastMessage = new BroadcastMessage(messenger, message);
            synchronized (broadcastMessageQueue) {
                broadcastMessageQueue.offer(broadcastMessage);
                broadcastMessageQueue.notifyAll();
            }
            return;
        }

        DirectMessage directMessage = new DirectMessage(messenger, addressee, message);

        synchronized (directMessageQueue) {
            directMessageQueue.offer(directMessage);
            directMessageQueue.notifyAll();
        }
    }

    /**
     * This method closes the chat. Any message not handled is lost.
     */
    public void closeChat(){
        if(isOpen){
            isOpen = false;
            synchronized (threadPool){
                for (Thread thread : threadPool){
                    thread.interrupt();
                }
            }
        }
    }

//region CHAT THREADS

    /**
     * Starts the thread that handles direct messages.
     */
    private void startDirectMessageThread(){
         Thread dmThread = new Thread(
                () -> {
                    while (isOpen){
                        DirectMessage directMessage;
                        synchronized (directMessageQueue) {
                            while (directMessageQueue.isEmpty()) {
                                try {
                                    directMessageQueue.wait();
                                } catch (InterruptedException e) {
                                    System.err.println("ERROR IN DIRECT CHAT: " + e.getMessage());
                                }
                            }
                            directMessage = directMessageQueue.poll();
                        }
                        VirtualClient addressee;
                        VirtualClient messengerClient;
                        synchronized (connectedClients) {
                             addressee = connectedClients.get(directMessage.addressee());
                             messengerClient = connectedClients.get(directMessage.messenger());
                        }

                        try {
                            if(addressee != null) {
                                String header = directMessage.messenger() + " TO " + directMessage.addressee();
                                addressee.displayMessage(header, directMessage.message());
                                if(!directMessage.addressee().equals(directMessage.messenger())) {
                                    messengerClient.displayMessage(header, directMessage.message());
                                }
                            }
                        } catch (RemoteException e){
                            centralServer.disconnect(directMessage.addressee() , addressee);
                        }
                    }
                }
        );

        dmThread.setDaemon(true);
        dmThread.start();
        synchronized (threadPool) {
            threadPool.add(dmThread);
        }
    }

    /**
     * Starts the thread that handles broadcast messages.
     */
    private void startBroadcastMessageThread(){
        Thread bmThread = new Thread(
                () -> {
                    List<String> disconnectingClients = new ArrayList<>(4);
                    BroadcastMessage broadcastMessage;
                    while (isOpen){
                        synchronized (broadcastMessageQueue) {
                            while (broadcastMessageQueue.isEmpty()) {
                                try {
                                    broadcastMessageQueue.wait();
                                } catch (InterruptedException e) {
                                    System.err.println("ERROR IN BROADCAST CHAT: " + e.getMessage());
                                }
                            }
                            broadcastMessage = broadcastMessageQueue.poll();
                        }
                        synchronized (connectedClients){
                            for(String user : connectedClients.keySet()){
                                try {
                                    VirtualClient addressee;
                                    synchronized(connectedClients) {
                                        addressee = connectedClients.get(user);
                                    }
                                    addressee.displayMessage(broadcastMessage.messenger(), broadcastMessage.message());
                                } catch (RemoteException e){
                                    disconnectingClients.add(broadcastMessage.messenger());
                                }
                            }
                        }
                        disconnectConnectionLossClients(disconnectingClients);
                    }
                }
        );

        bmThread.setDaemon(true);
        bmThread.start();
        synchronized (threadPool) {
            threadPool.add(bmThread);
        }
    }

//endregion

    /**
     * Disconnects the clients that have lost connection.
     * @param disconnectedClients list of clients that have lost connection
     */
    private void disconnectConnectionLossClients(List<String> disconnectedClients) {
        for(String nickname: disconnectedClients){
            VirtualClient client;
            synchronized (connectedClients){
                client = connectedClients.get(nickname);
            }

            try {
                CentralServer.getSingleton().disconnect(nickname, client);
                client.notifyIndirectDisconnect();
            } catch (RemoteException | IllegalStateException | IllegalArgumentException ignored){}
        }
        disconnectedClients.clear();
    }

}
