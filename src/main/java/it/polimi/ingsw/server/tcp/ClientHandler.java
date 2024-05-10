package it.polimi.ingsw.server.tcp;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.PlayerColor;
import it.polimi.ingsw.server.CentralServer;
import it.polimi.ingsw.server.Commands.*;
import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.VirtualServer;
import it.polimi.ingsw.server.tcp.message.*;
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
        serverRef = CentralServer.getSingleton();
        proxy = new ClientProxy(this, outputStream);
        initializeCommandExecutor();
    }

    private void initializeCommandExecutor(){
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
                            System.out.println("Executing " + command + "...");
                            command.execute(this, proxy);
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
                TCPClientMessage commandFromClient;
                commandFromClient = (TCPClientMessage) inputStream.readObject();
                synchronized (commandQueue) {
                    commandQueue.offer(commandFromClient);
                    commandQueue.notifyAll();
                    System.out.println("Added " + commandFromClient + " to the queue");
                }
            }
        } catch (IOException e) {
            closeSocket();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void validateClient(String nickname, VirtualClient client) throws IllegalStateException{
        if(!client.equals(serverRef.getClientFromNickname(nickname)))
            throw new IllegalStateException("Illegal request, wrong client!");
    }

    private void issueCommand(GameCommand command) throws IllegalStateException{
        try {
            serverRef.issueGameCommand(command);
        }catch (InterruptedException e) {
            throw new IllegalStateException("Couldn't issue command");
        }
    }

    @Override
    public void connect(String nickname, VirtualClient client){
        try {
            serverRef.connect(nickname, client);
            proxy.setUsername(nickname);
            serverRef.updateMsg(nickname + " has connected");
            proxy.updateError(new CheckConnectMessage(nickname, false));
        } catch (IllegalStateException stateException){
            proxy.updateError(new CheckConnectMessage(nickname, true, stateException.getMessage()));
        }
    }


    @Override
    public void setNumOfPlayers(String nickname, VirtualClient client, int num) throws IllegalStateException {
        validateClient(nickname, client);
        issueCommand(new SetNumOfPlayersCmd(serverRef.getGameRef(), nickname, num));
    }

    @Override
    public void disconnect(String nickname, VirtualClient client) {
        validateClient(nickname,client);
        try {
            serverRef.disconnect(nickname, client);
            serverRef.updateMsg(nickname + " has disconnected");
            proxy.updateError(new CheckDisconnectMessage(nickname, false));
        } catch (IllegalStateException stateException){
            proxy.updateError(new CheckDisconnectMessage(nickname,true, stateException.getMessage()));
        }
    }

    @Override
    public void placeStartCard(String nickname, VirtualClient client, boolean placeOnFront) throws IllegalStateException {
        validateClient(nickname, client);
        issueCommand(new PlaceStartingCmd(serverRef.getGameRef(), nickname, placeOnFront));
    }

    @Override
    public void chooseColor(String nickname, VirtualClient client, char colour) throws IllegalStateException {
        validateClient(nickname, client);
        issueCommand(new ChooseColorCmd(serverRef.getGameRef(), nickname, PlayerColor.parseColour(colour)));
    }

    @Override
    public void chooseObjective(String nickname, VirtualClient client, int choice) throws IllegalStateException {
        validateClient(nickname, client);
        issueCommand(new ChooseObjCmd(serverRef.getGameRef(), nickname, choice));
    }

    @Override
    public void placeCard(String nickname, VirtualClient client, String cardID, Point placePos, CornerDirection cornerDir, boolean placeOnFront) throws IllegalStateException {

    }

    @Override
    public void draw(String nickname, VirtualClient client, char deck, int card) throws IllegalStateException {
        validateClient(nickname, client);
        issueCommand(new DrawCmd(serverRef.getGameRef(), nickname, deck, card));
    }

    @Override
    public void startGame(String nickname, VirtualClient client) throws IllegalStateException {
        validateClient(nickname, client);
        issueCommand(new StartGameCmd(serverRef.getGameRef(), nickname));
    }

    @Override
    public void sendMsg(String nickname, VirtualClient client, String message) {
        validateClient(nickname, client);
        String fullMessage = nickname + ": " + message;
        System.out.println(fullMessage);
        serverRef.updateMsg(fullMessage);
    }


    @Override
    public void ping() throws RemoteException {
        System.out.println("Server was pinged from " + connectionSocket.getInetAddress());
        try{
            outputStream.writeObject(new PingMessage(true));
            outputStream.flush();
        } catch (IOException e) {
            throw new RemoteException("Can't ping client", e);
        }
    }


    @Override
    public void testCmd(String nickname, VirtualClient rmiClient, String text) throws RemoteException {

    }

    void closeSocket(){
        try {
            // TODO check if checks are needed
            if(inputStream != null){
                inputStream.close();
            }

            if(outputStream != null){
                outputStream.close();
            }

            if(connectionSocket != null) {
                connectionSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }
}
