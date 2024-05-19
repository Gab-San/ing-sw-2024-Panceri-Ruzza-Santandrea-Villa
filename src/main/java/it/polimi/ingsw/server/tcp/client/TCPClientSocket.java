package it.polimi.ingsw.server.tcp.client;

import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.PlayerColor;
import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.tcp.message.*;
import it.polimi.ingsw.server.tcp.message.TCPServerCheckMessage;
import it.polimi.ingsw.server.tcp.message.TCPServerMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.*;

public class TCPClientSocket implements VirtualClient{

    private final Socket clientSocket;
    private final ObjectInputStream inputStream;
    private final Queue<TCPServerMessage> updateQueue;
    private final ServerProxy proxy;
    public TCPClientSocket(int port) throws IOException {
        this("localhost", port);
    }

    public TCPClientSocket(String hostAddr, int connectionPort) throws IOException{
        this.clientSocket = new Socket(hostAddr, connectionPort);
        this.proxy = new ServerProxy(new ObjectOutputStream(clientSocket.getOutputStream()), this);
        inputStream = new ObjectInputStream(clientSocket.getInputStream());
        updateQueue = new LinkedBlockingQueue<>();
        startReader();
        startCommandExecutor();
    }
//region SOCKET THREADS

    private void startCommandExecutor(){
        new Thread(
                () -> {
                    while (!clientSocket.isClosed()){
                        TCPServerMessage command;
                        synchronized (updateQueue) {
                            while (updateQueue.isEmpty()) {
                                try {
                                    updateQueue.wait();
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }

                            command = updateQueue.remove();
                        }
                        try {
                            command.execute(this);
                        } catch (RemoteException e) {
                            System.err.println(e.getMessage() + "\n" + e.getCause().getMessage());
                            closeSocket();
                        }
                    }
                }
        ).start();
    }


    public void startReader() {
        new Thread(
                () -> {
                    System.out.println("Socket connection started!");
                    try {
                        while (!clientSocket.isClosed()) {
                            TCPMessage commandFromServer;
                            commandFromServer = (TCPMessage) inputStream.readObject();
                            if (!commandFromServer.isCheck()) {
                                synchronized (updateQueue) {
                                    updateQueue.offer((TCPServerMessage) commandFromServer);
                                    updateQueue.notifyAll();
                                }
                            } else {
                                proxy.addCheck((TCPServerCheckMessage) commandFromServer);
                            }
                        }
                    } catch (IOException e) {
                        closeSocket();
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
        ).start();
    }

//endregion

//region FOR TEST PURPOSES
public boolean isClosed(){
        return clientSocket.isClosed();
    }

//    void sendObject(Object obj){
//        try{
//            outputStream.writeObject(obj);
//            outputStream.flush();
//        } catch (IOException exception){
//            closeSocket();
//        }
//    }

//endregion

//region SOCKET FUNCTIONS
    public void closeSocket(){
        try{
            if(!clientSocket.isClosed()) {
                if (inputStream != null) inputStream.close();

                clientSocket.close();

            }
        } catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

    public ServerProxy getProxy(){
        return proxy;
    }
//endregion

//region VIRTUAL CLIENT INTERFACE
    @Override
    public void update(String msg){
        System.out.println(msg);
        System.out.flush();
    }

    @Override
    public void ping() throws RemoteException {
        return;
    }

    @Override
    public void updatePlayer(String nickname, PlayerColor colour) throws RemoteException {

    }

    @Override
    public void updatePlayer(String nickname, int playerTurn) throws RemoteException {

    }

    @Override
    public void updatePlayer(String nickname, boolean isConnected) throws RemoteException {

    }

    @Override
    public void createPlayer(String nickname, boolean isConnected, int turn, PlayerColor colour) throws RemoteException {

    }

    @Override
    public void deckReveal(char deck, String revealedId, int cardPosition) throws RemoteException {

    }

    @Override
    public void createDeck(char deck, String topId, String firstId, String secondId) throws RemoteException {

    }

    @Override
    public void deckUpdate(char deck, String cardID) throws RemoteException {

    }

    @Override
    public void emptyDeck(char deck) throws RemoteException {

    }

    @Override
    public void updatePhase(GamePhase gamePhase) throws RemoteException {

    }

    @Override
    public void updateScore(String nickname, int score) throws RemoteException {

    }

    @Override
    public void updateTurn(int currentTurn) throws RemoteException {

    }

//endregion
}
