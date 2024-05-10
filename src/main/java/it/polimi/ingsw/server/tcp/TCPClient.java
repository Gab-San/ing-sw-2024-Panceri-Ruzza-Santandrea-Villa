package it.polimi.ingsw.server.tcp;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.server.CommandPassthrough;
import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.tcp.message.*;

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
    private final Queue<TCPServerErrorMessage> errorChecks;
    private String nickname;
    public TCPClient(String hostAddr,int objPort) throws IOException, UnknownHostException, IllegalArgumentException {
        this.clientSocket = new Socket(hostAddr, objPort);
        outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        inputStream = new ObjectInputStream(clientSocket.getInputStream());
        updateQueue = new LinkedBlockingQueue<>();
        errorChecks = new LinkedBlockingQueue<>();
        startReader();
        startCommandExecutor();
        startErrorExecutor();
    }

    private void startErrorExecutor() {
        new Thread(
                () -> {
                    while(!clientSocket.isClosed()){
                        TCPServerErrorMessage errorMessage;
                        synchronized (errorChecks){
                            while(errorChecks.isEmpty()){
                                try{
                                    errorChecks.wait();
                                } catch (InterruptedException e){
                                    throw new RuntimeException(e);
                                }
                            }

                            errorMessage = errorChecks.remove();
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
                            System.out.println("Executing " + command + "...");
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
                    System.out.println("Client started!");
                    try {
                        while (!clientSocket.isClosed()) {
                            TCPMessage commandFromServer;
                            commandFromServer = (TCPMessage) inputStream.readObject();
                            if (!commandFromServer.isError()) {
                                synchronized (updateQueue) {
                                    updateQueue.offer((TCPServerMessage) commandFromServer);
                                    updateQueue.notifyAll();
                                    System.out.println("Added " + commandFromServer + " to the queue");
                                }
                            } else {
                                synchronized (errorChecks) {
                                    errorChecks.offer((TCPServerErrorMessage) commandFromServer);
                                    errorChecks.notifyAll();
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

    boolean isClosed(){
        return clientSocket.isClosed();
    }

    @Override
    public void update(String msg){
        System.out.println(msg);
        System.out.flush();
    }
    public void updateError(String errorMsg){
        System.err.println(errorMsg);
        System.err.flush();
    }
    @Override
    public void ping() throws RemoteException {
        try{
            outputStream.writeObject(new PingMessage(true));
            outputStream.flush();
        } catch (IOException e) {
            throw new RemoteException("Can't ping server", e);
        }
    }

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

    public void setNickname(String nickname){
        if(this.nickname == null) {
            this.nickname = nickname;
        }
    }

    private void pingServer() throws RemoteException {
        try{
            outputStream.writeObject(new PingMessage());
            outputStream.flush();
        } catch (IOException exception){
            closeSocket();
            throw new RemoteException("Connection Lost: " + exception.getMessage());
        }
    }

    private void validateConnection() throws IllegalStateException, RemoteException{
        if(nickname == null){
            throw new IllegalStateException("Please connect to server before sending commands other than 'connect'.");
        }
        else pingServer();
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

    @Override
    public void sendMsg(String msg) throws RemoteException {
        sendCommand(new SendMessage(nickname, msg));
    }

    @Override
    public void testCmd(String text) throws RemoteException {
        sendCommand(new TestCmdMessage(nickname, text));
    }

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
    public void setNumOfPlayers(int num) throws IllegalStateException, RemoteException {
        sendCommand(new SetNumofPlayerMessage(nickname,num));
    }

    @Override
    public void disconnect() throws IllegalStateException, RemoteException {
        sendCommand(new DisconnectMessage(nickname));
        pingServer();
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
}
