package it.polimi.ingsw.server.tcp.client;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.server.CommandPassthrough;
import it.polimi.ingsw.server.tcp.message.TCPClientMessage;
import it.polimi.ingsw.server.tcp.message.*;
import it.polimi.ingsw.server.tcp.message.PingMessage;
import it.polimi.ingsw.server.tcp.message.TCPServerCheckMessage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientSideProxy implements CommandPassthrough {
    private final ObjectOutputStream outputStream;
    private String nickname;
    private final TCPClientSocket client;
    private final Queue<TCPServerCheckMessage> checkQueue;
    public ClientSideProxy(ObjectOutputStream outputStream, TCPClientSocket client){
        this.outputStream = outputStream;
        this.client = client;
        checkQueue = new LinkedBlockingQueue<>();
        startErrorExecutor();
    }

    private void startErrorExecutor() {
        new Thread(
                () -> {
                    while(!client.isClosed()){
                        TCPServerCheckMessage checkMessage;
                        synchronized (checkQueue){
                            while(checkQueue.isEmpty()){
                                try{
                                    checkQueue.wait();
                                } catch (InterruptedException e){
                                    throw new RuntimeException(e);
                                }
                            }

                            checkMessage = checkQueue.remove();
                        }
                        checkMessage.handle(this);
                    }
                }
        ).start();
    }

//region PROXY FUNCTIONS
    void addCheck(TCPServerCheckMessage checkMessage){
        synchronized (checkQueue) {
            checkQueue.offer(checkMessage);
            checkQueue.notifyAll();
        }
    }
//endregion

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
            client.closeSocket();
            throw new RemoteException("Connection Lost: " + exception.getMessage());
        }
    }

    private void waitForCheck() throws IllegalStateException{
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
            this.nickname = nickname;
        } catch (IOException e) {
            client.closeSocket();
            throw new RemoteException("Connection Lost: " + e.getMessage());
        }
    }

    @Override
    public void disconnect() throws IllegalStateException, IllegalArgumentException, RemoteException {
        sendCommand(new DisconnectMessage(nickname));
        waitForCheck();
        client.closeSocket();
    }

    @Override
    public void sendMsg(String msg) throws RemoteException {
        sendCommand(new SendMessage(nickname, msg));
    }

    @Override
    public void setNumOfPlayers(int num) throws RemoteException {
        sendCommand(new SetNumofPlayerMessage(nickname,num));
    }



    @Override
    public void chooseColor(char color) throws RemoteException {
        sendCommand(new ChooseMessage(nickname, color));
    }

    @Override
    public void chooseObjective(int choice) throws RemoteException {
        sendCommand(new ChooseMessage(nickname, choice));
    }

    @Override
    public void draw(char deck, int card) throws RemoteException {
        sendCommand(new DrawMessage(nickname, deck, card));
    }


    @Override
    public void placeStartCard(boolean placeOnFront) throws RemoteException {
        sendCommand(new PlaceStartingCardMessage(nickname,placeOnFront));
    }


    @Override
    public void placeCard(String cardID, Point placePos, String cornerDir, boolean placeOnFront) throws RemoteException {
        sendCommand(new PlaceCardMessage(nickname, cardID,
                placePos.row(), placePos.col(),cornerDir,placeOnFront));
    }

    @Override
    public void startGame(int numOfPlayers) throws RemoteException {
        sendCommand(new RestartGameMessage(nickname, numOfPlayers));
    }
//endregion
}
