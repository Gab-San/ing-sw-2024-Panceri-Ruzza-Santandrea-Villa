package it.polimi.ingsw.server.tcp;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.server.CommandPassthrough;
import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.tcp.message.*;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;

public class TCPClient implements CommandPassthrough, VirtualClient {
    private final Socket clientSocket;
    private final Socket messageSocket;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private final BufferedReader reader;
    private final PrintWriter writer;
    public String nickname;
    public TCPClient(String hostAddr, int objPort, int msgPort) throws IOException {
        this.clientSocket = new Socket(hostAddr, objPort);
        this.messageSocket = new Socket(hostAddr, msgPort);
        this.inputStream = new ObjectInputStream(clientSocket.getInputStream());
        this.outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        this.reader = new BufferedReader(new InputStreamReader(messageSocket.getInputStream()));
        this.writer = new PrintWriter(messageSocket.getOutputStream(), true);
        start();
        setupInputThread();
    }

    private void setupInputThread(){
        Thread inputStreamReader = new Thread(
                () -> {
                    while (true) {
                        try {
                            TCPMessage objRead;
                            while ((objRead = (TCPMessage) inputStream.readObject()) != null) {
                                objRead.getCommand(this);
                            }
                        } catch (IOException ioException) {
                            throw new RuntimeException("Error while reading", ioException);
                        } catch (ClassNotFoundException classException) {
                            throw new RuntimeException("Class Not Found", classException);
                        }
                    }
                }
        );
        inputStreamReader.setDaemon(true);
        inputStreamReader.start();
    }

    void start() throws IOException {
        try {
            System.out.println(reader.readLine());
        } catch (IOException e) {
            close();
        }
    }

    @Override
    public void sendMsg(String msg) throws RemoteException {

    }

    @Override
    public void testCmd(String text) throws RemoteException {
        validateConnection();
        TestMessage test = new TestMessage(nickname, text);
            try {
                outputStream.writeObject(test);
                outputStream.flush();
            } catch (IOException e) {
                throw new RemoteException("Error while sending test message", e);
        }
    }

    @Override
    public void connect(String nickname) throws IllegalStateException, RemoteException {
        try{
            outputStream.writeObject(new ConnectMessage(nickname));
            outputStream.flush();
        } catch (IOException ioException){
            throw new RemoteException("Connection Lost: ", ioException);
        }

        try{
            String updateMsg;
            if ((updateMsg = reader.readLine()) != null){
                String msg = updateMsg.split("\\s+")[0];
                if(msg.equalsIgnoreCase("ERROR")){
                    throw new IllegalStateException(updateMsg);
                }
                System.out.println(updateMsg);
                this.nickname = nickname;
            }
        } catch (IOException ioException){
            throw new RemoteException("Connection Lost: ", ioException);
        }
    }

    @Override
    public void setNumOfPlayers(int num) throws IllegalStateException, RemoteException {

    }

    @Override
    public void disconnect() throws IllegalStateException, RemoteException {

    }

    @Override
    public void placeStartCard(boolean placeOnFront) throws IllegalStateException, RemoteException {

    }

    @Override
    public void chooseColor(char color) throws IllegalStateException, RemoteException {

    }

    @Override
    public void chooseObjective(int choice) throws IllegalStateException, RemoteException {

    }

    @Override
    public void placeCard(String cardID, Point placePos, CornerDirection cornerDir, boolean placeOnFront) throws IllegalStateException, RemoteException {

    }

    @Override
    public void draw(char deck, int card) throws IllegalStateException, RemoteException {

    }

    @Override
    public void startGame() throws IllegalStateException, RemoteException {

    }

    @Override
    public void update(String msg) throws RemoteException {

    }

    @Override
    public void ping() throws RemoteException {
        synchronized (writer){
            writer.println("Ping received...");
            writer.flush();
        }
    }

    public void closeConnection() throws RemoteException {
        ExitMessage exitMessage = new ExitMessage();
        try{
            outputStream.writeObject(exitMessage);
            close();
        } catch (IOException e) {
            throw new RemoteException("Error while exiting", e);
        }
    }

    private void validateConnection() throws IllegalStateException, RemoteException{
        if(nickname == null){
            throw new IllegalStateException("Please connect to server before sending commands other than 'connect'.");
        }
        else pingServer(); // throws RemoteException on connection lost.
    }

    private void pingServer() throws RemoteException{
        try {
            outputStream.writeObject(new PingMessage());
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try{
            String updateMsg;
            if ((updateMsg = reader.readLine()) != null){
                String msg = updateMsg.split("\\s+")[0];
                if(msg.equalsIgnoreCase("ping")) {
                    System.out.println(updateMsg);
                }
            } else {
                throw new RemoteException("Connection Lost: ");
            }
        } catch (IOException ioException){
            throw new RemoteException("Connection Lost: ", ioException);
        }
    }

    @Override
    public void close(){
        try {
            inputStream.close();
            outputStream.close();
            reader.close();
            clientSocket.close();
            messageSocket.close();
        } catch (IOException ioException){
            throw new RuntimeException(ioException);
        }
    }
}
