package it.polimi.ingsw.network.tcp.client;

import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.network.CommandPassthrough;
import it.polimi.ingsw.network.tcp.message.TCPClientMessage;
import it.polimi.ingsw.network.tcp.message.TCPServerCheckMessage;
import it.polimi.ingsw.network.tcp.message.commands.*;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * This class implements the command pass through interface using socket connection protocol
 * and functions as a server proxy.
 *
 * <p>
 *     This class effectively hides the network calls structure and gives more space
 *     to add logic on the client-side.
 * </p>
 */
public class TCPServerProxy implements CommandPassthrough {
    private final ObjectOutputStream outputStream;
    private String nickname;
    private final TCPClientSocket clientSocket;
    private final Queue<TCPServerCheckMessage> checkQueue;

    /**
     * Constructs the tcp server proxy.
     * @param outputStream wrapped socket output stream
     * @param clientSocket client socket this proxy is bound to
     */
    public TCPServerProxy(ObjectOutputStream outputStream, TCPClientSocket clientSocket){
        this.outputStream = outputStream;
        this.clientSocket = clientSocket;
        checkQueue = new ArrayBlockingQueue<>(2);
    }

//region PROXY FUNCTIONS

    /**
     * Adds a check message to the queue of check messages.
     * @param checkMessage check message to be handled
     */
    void addCheck(TCPServerCheckMessage checkMessage){
        synchronized (checkQueue) {
            checkQueue.offer(checkMessage);
            checkQueue.notifyAll();
        }
    }

    /**
     * Closes proxy and socket, effectively ending the connection.
     */
    void closeProxy(){
        try{
            if(outputStream != null) outputStream.close();
        } catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

    /**
     * Returns the nickname of this proxy. Used for testing purposes.
     * @return nickname with which the user connected to the server.
     */
    String getNickname(){
        return nickname;
    }
//endregion

//region AUXILIARY FUNCTIONS
    /**
     * Validates proxy connection by checking if the client has ever subscribed to the server or by pinging.
     * @throws IllegalStateException if the client has never subscribed to the server
     * @throws RemoteException if an error occurs during connection
     */
    private void validateConnection() throws IllegalStateException, RemoteException{
        if(nickname == null){
            throw new IllegalStateException("Connect or wait for connection before sending more commands");
        } else ping();
    }

    /**
     * Sends a command to the server.
     * @param command command message to send
     * @throws RemoteException if an error while sending message
     */
    private void sendCommand(TCPClientMessage command) throws RemoteException{
        validateConnection();
        try{
            outputStream.writeObject(command);
            outputStream.flush();
            outputStream.reset();
        } catch (IOException exception){
            clientSocket.closeSocket();
            throw new RemoteException("Connection Lost: " + exception.getMessage());
        }
    }

    /**
     * Synchronizes the tcp call waiting for a check from the server.
     * @throws IllegalStateException exception brought by the check
     * @throws IllegalArgumentException exception brought by the check
     */
    private void waitForCheck() throws IllegalStateException, IllegalArgumentException{
        TCPServerCheckMessage checkMessage;
        do{
            try{
                Thread.sleep(500);
            } catch (InterruptedException interruptedException){
                throw new RuntimeException(interruptedException);
            }

            synchronized (checkQueue){
                checkMessage = checkQueue.poll();
            }
        } while(checkMessage == null);
        checkMessage.handle(this);
    }
//endregion

//region VIRTUAL SERVER IMPLEMENTATION

    @Override
    public void ping() throws RemoteException {
        try{
            outputStream.writeObject(new PingMessage());
            outputStream.flush();
            outputStream.reset();
        } catch (IOException e) {
            throw new RemoteException("Can't ping server", e);
        }
    }

    @Override
    public void connect(String nickname) throws IllegalStateException, RemoteException {
        try{
            outputStream.writeObject(new ConnectMessage(nickname));
            outputStream.flush();
            waitForCheck();
            outputStream.reset();
            this.nickname = nickname;
        } catch (IOException e) {
            clientSocket.closeSocket();
            throw new RemoteException("Connection Lost: " + e.getMessage());
        }
    }

    @Override
    public void disconnect() throws IllegalStateException, IllegalArgumentException, RemoteException {
        sendCommand(new DisconnectMessage(nickname));
        waitForCheck();
        clientSocket.closeSocket();
    }

    @Override
    public void sendMsg(String addressee, String message) throws RemoteException {
        sendCommand(new SendMessage(nickname, addressee, message));
    }

    @Override
    public void setNumOfPlayers(int num) throws RemoteException {
        sendCommand(new SetNumofPlayerMessage(nickname,num));
    }



    @Override
    public void chooseColor(char colour) throws RemoteException {
        sendCommand(new ChooseMessage(nickname, colour));
    }

    @Override
    public void chooseObjective(int choice) throws RemoteException {
        sendCommand(new ChooseMessage(nickname, choice));
    }

    @Override
    public void draw(char deck, int cardPosition) throws RemoteException {
        sendCommand(new DrawMessage(nickname, deck, cardPosition));
    }


    @Override
    public void placeStartCard(boolean placeOnFront) throws RemoteException {
        sendCommand(new PlaceStartingCardMessage(nickname,placeOnFront));
    }


    @Override
    public void placeCard(String cardID, GamePoint placePos, String cornerDir, boolean placeOnFront) throws RemoteException {
        sendCommand(new PlaceCardMessage(nickname, cardID,
                placePos.row(), placePos.col(),cornerDir,placeOnFront));
    }

    @Override
    public void restartGame(int numOfPlayers) throws RemoteException {
        sendCommand(new RestartGameMessage(nickname, numOfPlayers));
    }
//endregion
}
