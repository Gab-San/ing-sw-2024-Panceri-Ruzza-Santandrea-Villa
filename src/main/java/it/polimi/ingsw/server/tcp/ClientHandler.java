package it.polimi.ingsw.server.tcp;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.server.CentralServer;
import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.VirtualServer;
import it.polimi.ingsw.server.tcp.message.PingMessage;
import it.polimi.ingsw.server.tcp.message.TCPMessage;

import java.io.*;
import java.rmi.RemoteException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public class ClientHandler extends Thread implements VirtualServer {
    boolean connected;
    private final CentralServer serverRef;
    private final BlockingQueue<Consumer<ClientHandler>> commands;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private final ClientProxy virtualClient;
    private final BufferedReader reader;

    public ClientHandler(ObjectInputStream inputStream, ObjectOutputStream outputStream,
                         PrintWriter writer, BufferedReader reader, CentralServer serverRef){
        this.serverRef = serverRef;
        this.commands = new LinkedBlockingQueue<>();
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.reader = reader;
        this.virtualClient = new ClientProxy(outputStream, writer, reader);
        virtualClient.start();
        this.connected = true;
    }

    @Override
    public void run() {
        Thread objReaderThread = new Thread(
                () -> {
                    while (connected) {
                        try {
                            TCPMessage objRead;
                            while ((objRead = (TCPMessage) inputStream.readObject()) != null) {
                                if (objRead.isExit()) {
                                    System.out.println("Closing connection...");
                                    tearDown();
                                    connected = false;
                                    break;
                                }
                                commands.put(objRead.getCommand(virtualClient));
                            }
                        } catch (IOException ioException) {
                            throw new RuntimeException("Error while reading", ioException);
                        } catch (ClassNotFoundException classException) {
                            throw new RuntimeException("Class Not Found", classException);
                        } catch (InterruptedException interruptedException) {
                            throw new RuntimeException("Command queue error", interruptedException);
                        }
                    }
                }
        );
        objReaderThread.setDaemon(true);
        objReaderThread.start();

        Thread commandExecutor = new Thread(
                () ->{
                    while (connected){
                        try {
                            commands.take().accept(this);
                        } catch (InterruptedException interruptedException) {
                            throw new RuntimeException("Command queue error",interruptedException);
                        }
                    }
                }
        );
        commandExecutor.setDaemon(true);
        commandExecutor.start();

        try {
            objReaderThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException("Thread error", e);
        }
    }

    @Override
    public void connect(String nickname, VirtualClient client) throws IllegalStateException, RemoteException {
        serverRef.connect(nickname, virtualClient);
    }

    @Override
    public void setNumOfPlayers(String nickname, VirtualClient client, int num) throws IllegalStateException, RemoteException {

    }

    @Override
    public void disconnect(String nickname, VirtualClient client) throws IllegalStateException, RemoteException {

    }

    @Override
    public void placeStartCard(String nickname, VirtualClient client, boolean placeOnFront) throws IllegalStateException, RemoteException {

    }

    @Override
    public void chooseColor(String nickname, VirtualClient client, char colour) throws IllegalStateException, RemoteException {

    }

    @Override
    public void chooseObjective(String nickname, VirtualClient client, int choice) throws IllegalStateException, RemoteException {

    }

    @Override
    public void placeCard(String nickname, VirtualClient client, String cardID, Point placePos, CornerDirection cornerDir, boolean placeOnFront) throws IllegalStateException, RemoteException {

    }

    @Override
    public void draw(String nickname, VirtualClient client, char deck, int card) throws IllegalStateException, RemoteException {

    }

    @Override
    public void startGame(String nickname, VirtualClient client) throws IllegalStateException, RemoteException {

    }

    @Override
    public void sendMsg(String nickname, VirtualClient client, String message) throws RemoteException {

    }

    @Override
    public void testCmd(String nickname, VirtualClient client, String text) throws RemoteException{
    }

    @Override
    public void ping() throws RemoteException {
        virtualClient.pingClient();
    }


    public void tearDown() {
        try {
            inputStream.close();
            outputStream.close();
            virtualClient.close();
        } catch (IOException ioException){
            System.err.println(ioException.getMessage());
        }
    }
}
