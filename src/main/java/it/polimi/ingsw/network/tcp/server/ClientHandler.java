package it.polimi.ingsw.network.tcp.server;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.PlayerColor;
import it.polimi.ingsw.network.CentralServer;
import it.polimi.ingsw.network.commands.*;
import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.VirtualServer;
import it.polimi.ingsw.network.tcp.message.TCPClientMessage;
import it.polimi.ingsw.network.tcp.message.*;

import it.polimi.ingsw.network.tcp.message.commands.PingMessage;

import it.polimi.ingsw.network.tcp.message.TCPClientCheckMessage;
import it.polimi.ingsw.network.tcp.message.check.CheckMessage;
import it.polimi.ingsw.network.tcp.message.error.ErrorMessage;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientHandler implements Runnable, VirtualServer {

    private final Socket connectionSocket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private final Queue<TCPClientMessage> commandQueue;
    private final Queue<TCPClientCheckMessage> errorQueue;
    private final CentralServer serverRef;
    private final ClientProxy proxy;
    public ClientHandler(Socket connectionSocket){
        this.connectionSocket = connectionSocket;
        try {
            this.outputStream = new ObjectOutputStream(connectionSocket.getOutputStream());
            this.inputStream = new ObjectInputStream(connectionSocket.getInputStream());
        } catch (IOException ioException){
            closeSocket();
        }
        commandQueue = new LinkedBlockingQueue<>();
        errorQueue = new LinkedBlockingQueue<>();
        serverRef = CentralServer.getSingleton();
        proxy = new ClientProxy(this, outputStream);
        startCommandExecutor();
//        startErrorExecutor();
    }
//region SOCKET THREADS

//TODO check if the startErrorExecutor is needed

//    private void startErrorExecutor() {
//        new Thread(
//                () -> {
//                    while(!connectionSocket.isClosed()){
//                        TCPClientCheckMessage errorMessage;
//                        synchronized (errorQueue){
//                            while(errorQueue.isEmpty()){
//                                try{
//                                    errorQueue.wait();
//                                } catch (InterruptedException e){
//                                    throw new RuntimeException(e);
//                                }
//                            }
//
//                            errorMessage = errorQueue.remove();
//                        }
//                        errorMessage.handle(this);
//                    }
//                }
//        ).start();
//    }

    private void startCommandExecutor(){
        new Thread(
                () -> {
                    while (!connectionSocket.isClosed()){
                        TCPClientMessage command;
                        synchronized (commandQueue) {
                            while (commandQueue.isEmpty()) {
                                try {
                                    commandQueue.wait();
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }

                            command = commandQueue.remove();
                        }


                        try {
                            command.execute(this, proxy);
                        } catch (IllegalStateException stateException) {
                            proxy.sendCheck(new CheckMessage(stateException));
                        } catch (IllegalArgumentException argException){
                            proxy.sendCheck(new CheckMessage(argException));
                        } catch (RemoteException e) {
                            System.err.println(e.getMessage() + "\n" + e.getCause().getMessage());
                            closeSocket();
                        }
                    }
                }
        ).start();
    }

    /**
     * Runs this operation.
     */
    @Override
    public void run() {
        try{
            while (!connectionSocket.isClosed()){
                TCPMessage commandFromClient;
                commandFromClient = (TCPMessage) inputStream.readObject();
                if(!commandFromClient.isCheck()) {
                    synchronized (commandQueue) {
                        commandQueue.offer((TCPClientMessage) commandFromClient);
                        commandQueue.notifyAll();
                    }
                } else{
                    synchronized (errorQueue) {
                        errorQueue.offer((TCPClientCheckMessage) commandFromClient);
                        errorQueue.notifyAll();
                    }
                }
            }
        } catch (IOException e) {
            closeSocket();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
//endregion

//region AUXILIARY FUNCTIONS
    private void validateClient(String nickname, VirtualClient client) throws RemoteException {
        if(!client.equals(serverRef.getClientFromNickname(nickname)))
            proxy.sendNotification(new ErrorMessage("Illegal request, wrong client!"));
    }


//endregion

//region VIRTUAL SERVER IMPLEMENTATION
    @Override
    public void connect(String nickname, VirtualClient client) throws IllegalStateException{
        serverRef.connect(nickname, client);
        proxy.setUsername(nickname);
        proxy.sendCheck(new CheckMessage());
    }


    @Override
    public void sendMsg(String nickname, VirtualClient client, String addressee, String message) {
        try {
            validateClient(nickname, client);
        } catch (IOException ioException){
            closeSocket();
        }
        serverRef.sendMessage(nickname, addressee, message);
    }

    @Override
    public void setNumOfPlayers(String nickname, VirtualClient client, int num)  {
        try {
            validateClient(nickname, client);
        } catch (IOException ioException){
            closeSocket();
        }
        serverRef.issueGameCommand(new SetNumOfPlayersCmd(serverRef.getGameRef(), nickname, num));
    }

    @Override
    public void disconnect(String nickname, VirtualClient client)
            throws IllegalStateException, IllegalArgumentException, RemoteException {
        try {
            validateClient(nickname, client);
        } catch (IOException ioException){
            closeSocket();
        }
        serverRef.disconnect(nickname, client);
        proxy.sendCheck(new CheckMessage());
        closeSocket();
    }

    @Override
    public void chooseColor(String nickname, VirtualClient client, char colour)  {
        try {
            validateClient(nickname, client);
        } catch (IOException ioException){
            closeSocket();
        }
        serverRef.issueGameCommand(new ChooseColorCmd(serverRef.getGameRef(), nickname,
                PlayerColor.parseColour(colour)));
    }

    @Override
    public void chooseObjective(String nickname, VirtualClient client, int choice) {
        try {
            validateClient(nickname, client);
        } catch (IOException ioException){
            closeSocket();
        }
        serverRef.issueGameCommand(new ChooseObjCmd(serverRef.getGameRef(), nickname, choice));
    }


    @Override
    public void draw(String nickname, VirtualClient client, char deck, int card) {
        try {
            validateClient(nickname, client);
        } catch (IOException ioException){
            closeSocket();
        }
        serverRef.issueGameCommand(new DrawCmd(serverRef.getGameRef(), nickname, deck, card));
    }

    @Override
    public void ping() throws RemoteException {
        try{
            outputStream.writeObject(new PingMessage());
            outputStream.flush();
            outputStream.reset();
        } catch (IOException e) {
            closeSocket();
        }
    }


    @Override
    public void placeStartCard(String nickname, VirtualClient client, boolean placeOnFront)  {
        try {
            validateClient(nickname, client);
        } catch (IOException ioException){
            closeSocket();
        }
        serverRef.issueGameCommand(new PlaceStartingCmd(serverRef.getGameRef(), nickname, placeOnFront));
    }




    @Override
    public void placeCard(String nickname, VirtualClient client, String cardID, int row,
                          int col, String cornerDir, boolean placeOnFront) throws IllegalStateException {
        serverRef.issueGameCommand(new PlacePlayCmd(
                serverRef.getGameRef(),
                nickname,
                cardID,
                new Point(row, col),
                CornerDirection.getDirectionFromString(cornerDir),
                placeOnFront
        ));
    }



    @Override
    public void restartGame(String nickname, VirtualClient client, int numOfPlayers) throws RemoteException {
        validateClient(nickname, client);
        serverRef.issueGameCommand(new RestartGameCmd(serverRef.getGameRef(), nickname, numOfPlayers));
    }


//endregion

//region SOCKET FUNCTIONS
    void closeSocket(){
        try {
            if(inputStream != null){
                inputStream.close();
            }

            if(outputStream != null){
                outputStream.close();
            }

            if(!connectionSocket.isClosed()) {
                connectionSocket.close();
            }
        } catch (IOException ignore) {
        }
    }

//endregion
}
