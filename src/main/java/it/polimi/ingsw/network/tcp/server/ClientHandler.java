package it.polimi.ingsw.network.tcp.server;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.network.CentralServer;
import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.VirtualServer;
import it.polimi.ingsw.network.commands.*;
import it.polimi.ingsw.network.tcp.message.TCPClientMessage;
import it.polimi.ingsw.network.tcp.message.TCPMessage;
import it.polimi.ingsw.network.tcp.message.check.CheckMessage;
import it.polimi.ingsw.network.tcp.message.error.ErrorMessage;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Thread that handles one tcp socket.
 * <p>
 *     Client handler implements virtual server interface and acts as
 *     the virtual server for the bound client socket.
 * </p>
 */
public class ClientHandler implements Runnable, VirtualServer {

    private final Socket connectionSocket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private final Queue<TCPClientMessage> commandQueue;
    private final CentralServer serverRef;
    private final ClientProxy proxy;

    /**
     * Constructs the client handler.
     * @param connectionSocket socket created when the connection was accepted
     */
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
        startCommandExecutor();
    }

//region SOCKET THREADS

    /**
     * Starts the thread that executes the commands.
     */
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
//                            closeSocket();
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
                }
            }
        } catch(EOFException eofException) {
            System.err.println("REACHED EOS!");
            closeSocket();
        }catch (IOException e) {
            closeSocket();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
//endregion

//region AUXILIARY FUNCTIONS
    /**
     * Client validation server-side.
     * @param nickname unique id of the user
     * @param client instance of virtual client
     * @throws RemoteException if an error occurs while notifying the client
     */
    private void validateClient(String nickname, VirtualClient client) throws RemoteException {
        if(!client.equals(serverRef.getClientFromNickname(nickname)))
            proxy.sendNotification(new ErrorMessage("Illegal request, wrong client!"));
    }


//endregion

//region VIRTUAL SERVER IMPLEMENTATION
    @Override
    public void connect(String nickname, VirtualClient client) throws IllegalStateException{
        serverRef.connect(nickname, client);
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
        serverRef.issueGameCommand(new SetNumOfPlayersCmd(serverRef.getGameController(), nickname, num));
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
        serverRef.issueGameCommand(new ChooseColorCmd(serverRef.getGameController(), nickname,
                PlayerColor.parseColour(colour)));
    }

    @Override
    public void chooseObjective(String nickname, VirtualClient client, int choice) {
        try {
            validateClient(nickname, client);
        } catch (IOException ioException){
            closeSocket();
        }
        serverRef.issueGameCommand(new ChooseObjCmd(serverRef.getGameController(), nickname, choice));
    }


    @Override
    public void draw(String nickname, VirtualClient client, char deck, int cardPosition) {
        try {
            validateClient(nickname, client);
        } catch (IOException ioException){
            closeSocket();
        }
        serverRef.issueGameCommand(new DrawCmd(serverRef.getGameController(), nickname, deck, cardPosition));
    }

    @Override
    public void ping() throws RemoteException {
        return;
    }


    @Override
    public void placeStartCard(String nickname, VirtualClient client, boolean placeOnFront)  {
        try {
            validateClient(nickname, client);
        } catch (IOException ioException){
            closeSocket();
        }
        serverRef.issueGameCommand(new PlaceStartingCmd(serverRef.getGameController(), nickname, placeOnFront));
    }




    @Override
    public void placeCard(String nickname, VirtualClient client, String cardID, int row,
                          int col, String cornerDir, boolean placeOnFront) throws IllegalStateException {
        serverRef.issueGameCommand(new PlacePlayCmd(
                serverRef.getGameController(),
                nickname,
                cardID,
                new GamePoint(row, col),
                CornerDirection.getDirectionFromString(cornerDir),
                placeOnFront
        ));
    }



    @Override
    public void restartGame(String nickname, VirtualClient client, int numOfPlayers) throws RemoteException {
        validateClient(nickname, client);
        serverRef.issueGameCommand(new RestartGameCmd(serverRef.getGameController(), nickname, numOfPlayers));
    }


//endregion

//region SOCKET FUNCTIONS

    /**
     * Closes this socket, ending the connection.
     */
    void closeSocket(){
        try {
            if(!connectionSocket.isClosed()) {
                inputStream.close();
                outputStream.close();
                connectionSocket.close();
            }
        } catch (IOException ignore) {
        }
    }

//endregion
}
