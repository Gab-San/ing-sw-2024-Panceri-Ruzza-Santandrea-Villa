package it.polimi.ingsw.network;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class ChatHandler{

    private final Map<String, VirtualClient> connectedClients;
    private final Queue<DirectMessage> directMessageQueue;
    private final Queue<BroadcastMessage> broadcastMessageQueue;
    private boolean isOpen;
    private final CentralServer centralServer;
    private final List<Thread> threadPool;

    public ChatHandler(CentralServer centralServer) {
        this.centralServer = centralServer;
        this.threadPool = new LinkedList<>();
        this.connectedClients = new HashMap<>();
        directMessageQueue = new LinkedBlockingQueue<>();
        isOpen = true;
        broadcastMessageQueue = new LinkedBlockingQueue<>();
        startDirectMessageThread();
        startBroadcastMessageThread();
    }


    public void addClient(String nickname, VirtualClient client){
        synchronized (connectedClients) {
            connectedClients.put(nickname, client);
        }
        addMessage("SERVER", "ALL", nickname + " has connected!" );
    }

    public void removeClient(String nickname){
        synchronized (connectedClients) {
            connectedClients.remove(nickname);
        }
        addMessage("SERVER", "all", "Disconnected " + nickname + " for connection loss");
    }

    public void addMessage(String messenger, String addressee, String message) throws IllegalArgumentException{
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
                        synchronized (connectedClients) {
                             addressee = directMessage.getAddresseeClient(connectedClients);
                        }
                        try {
                            addressee.displayMessage(directMessage.messenger(), directMessage.message());
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
    private void disconnectConnectionLossClients(List<String> disconnectedClients) {
        for(String nickname: disconnectedClients){
            VirtualClient client;
            synchronized (connectedClients){
                client = connectedClients.get(nickname);
            }
            try {
                centralServer.disconnect(nickname, client);
            } catch (IllegalStateException ignore){}
        }
        disconnectedClients.clear();
    }

}
