package it.polimi.ingsw.server.tcp;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.server.CommandPassthrough;
import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.tcp.message.*;
import it.polimi.ingsw.server.tcp.message.errors.PingMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.*;

public class TCPClient implements CommandPassthrough, VirtualClient{

    private final Socket clientSocket;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private final Queue<TCPServerMessage> updateQueue;
    private final Queue<TCPServerErrorMessage> errorQueue;
    private String nickname;
    public TCPClient(String hostAddr,int objPort) throws IOException, UnknownHostException, IllegalArgumentException {
        this.clientSocket = new Socket(hostAddr, objPort);
        outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        inputStream = new ObjectInputStream(clientSocket.getInputStream());
        updateQueue = new LinkedBlockingQueue<>();
        errorQueue = new LinkedBlockingQueue<>();
        startReader();
        startCommandExecutor();
        startErrorExecutor();
    }
//region SOCKET THREADS
    private void startErrorExecutor() {
        new Thread(
                () -> {
                    while(!clientSocket.isClosed()){
                        TCPServerErrorMessage errorMessage;
                        synchronized (errorQueue){
                            while(errorQueue.isEmpty()){
                                try{
                                    errorQueue.wait();
                                } catch (InterruptedException e){
                                    throw new RuntimeException(e);
                                }
                            }

                            errorMessage = errorQueue.remove();
                        }
                        errorMessage.handle(this);
                    }
                }
        ).start();
    }

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
                            if (!commandFromServer.isError()) {
                                synchronized (updateQueue) {
                                    updateQueue.offer((TCPServerMessage) commandFromServer);
                                    updateQueue.notifyAll();
                                }
                            } else {
                                synchronized (errorQueue) {
                                    errorQueue.offer((TCPServerErrorMessage) commandFromServer);
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
        ).start();
    }

//endregion

//region FOR TEST PURPOSES
    boolean isClosed(){
        return clientSocket.isClosed();
    }

    String getNickname(){
        return nickname;
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
    void closeSocket(){
        try{
            if(outputStream != null) {
                outputStream.close();
            }
            if(inputStream != null) {
                inputStream.close();
            }
            if(clientSocket != null){
                clientSocket.close();
            }
        } catch (IOException e){
            e.printStackTrace(System.err);
        }
    }

//endregion

//region VIRTUAL CLIENT INTERFACE
    @Override
    public void update(String msg){
        System.out.println(msg);
        System.out.flush();
    }
    public void updateError(String errorMsg){
        System.err.println(errorMsg);
        System.err.flush();
    }

//endregion


    public void establishConnection(String nickname){
        if(this.nickname == null) {
            this.nickname = nickname;
        }
        System.out.println("Connection established, now can send commands");
    }

//region AUXILIARY FUNCTIONS

    private void validateConnection() throws IllegalStateException, RemoteException{
        if(nickname == null){
            throw new IllegalStateException("Connect or wait for connection before sending more commands");
        } else ping();
    }

    private void sendCommand(TCPClientMessage command) throws RemoteException{
        validateConnection();
        try{
            outputStream.writeObject(command);
            outputStream.flush();
        } catch (IOException exception){
            closeSocket();
            throw new RemoteException("Connection Lost: " + exception.getMessage());
        }
    }
//endregion

//region VIRTUAL SERVER INTERFACE
    //region IMPLEMENTED & TESTED
    @Override
    public void connect(String nickname) throws IllegalStateException, RemoteException {
        try{
            outputStream.writeObject(new ConnectMessage(nickname));
            outputStream.flush();
        } catch (IOException e) {
            closeSocket();
            throw new RemoteException("Connection Lost: " + e.getMessage());
        }
    }

    @Override
    public void sendMsg(String msg) throws RemoteException {
        sendCommand(new SendMessage(nickname, msg));
    }

    @Override
    public void testCmd(String text) throws RemoteException {
        sendCommand(new TestCmdMessage(nickname, text));
    }

    @Override
    public void ping() throws RemoteException {
        try{
            outputStream.writeObject(new PingMessage());
            outputStream.flush();
        } catch (IOException e) {
            throw new RemoteException("Can't ping server", e);
        }
    }

    //endregion
    @Override
    public void setNumOfPlayers(int num) throws IllegalStateException, RemoteException {
        sendCommand(new SetNumofPlayerMessage(nickname,num));
    }




    @Override
    public void disconnect() throws IllegalStateException, RemoteException {
        sendCommand(new DisconnectMessage(nickname));
        ping();
        closeSocket();
    }

    @Override
    public void placeStartCard(boolean placeOnFront) throws IllegalStateException, RemoteException {

    }

    @Override
    public void chooseColor(char color) throws IllegalStateException, RemoteException {
        sendCommand(new ChooseMessage(nickname, color));
    }

    @Override
    public void chooseObjective(int choice) throws IllegalStateException, RemoteException {
        sendCommand(new ChooseMessage(nickname, choice));
    }

    @Override
    public void placeCard(String cardID, Point placePos, CornerDirection cornerDir, boolean placeOnFront) throws IllegalStateException, RemoteException {

    }

    @Override
    public void draw(char deck, int card) throws IllegalStateException, RemoteException {
        sendCommand(new DrawMessage(nickname, deck, card));
    }

    @Override
    public void startGame() throws IllegalStateException, RemoteException {
        new RestartGameMessage(nickname);
    }
//endregion
}
