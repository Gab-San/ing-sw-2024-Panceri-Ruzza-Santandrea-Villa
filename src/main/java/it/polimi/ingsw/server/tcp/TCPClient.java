package it.polimi.ingsw.server.tcp;

import it.polimi.ingsw.server.tcp.message.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class TCPClient extends Thread implements VirtualClientTest {

    Socket connectionSocket;
    private final ServerProxy server;
    private final BufferedReader reader;

    /**
     *
     * @param hostname the host server name
     * @param port the port of the socket
     * @throws IOException on BufferedReader and PrintWriter instantiation
     */
    public TCPClient(String hostname, int port) throws IOException {
        try{
            this.connectionSocket = new Socket(hostname, port);
        } catch (UnknownHostException unknownHostException){
            unknownHostException.printStackTrace(System.err);
        } catch (IOException ioException){
            System.err.println("ERROR: " + ioException.getMessage());
            ioException.printStackTrace(System.err);
        }

        this.reader = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
        this.server = new ServerProxy(new PrintWriter(connectionSocket.getOutputStream(), true));
    }

    @Override
    public void run() {
        // THIS IS THE WRITING THREAD
        Thread writerThread = new Thread(() -> {
            while (true) {
                Scanner inputScanner = new Scanner(System.in);
                System.out.println("Write send to send a msg");
                String command = inputScanner.nextLine();
                evaluate(command);
            }
        });
        writerThread.setDaemon(true);
        writerThread.start();

        //Reader Thread
        while (true){
            try {
                String inputLine;
                while ((inputLine = reader.readLine()) != null) {
                    System.out.println(inputLine);
                }
            } catch (IOException e) {
                System.err.println("ERROR: " + e.getMessage());
            }
        }
    }

    @Override
    public void showMsg(TCPMessage message) {
        System.out.println("[ECHO] " + message);
    }


    private void evaluate(String command) throws NoSuchElementException {
        String[] commandComponents = command.trim().split("\\s+");
        String keyCommand = Arrays.stream(commandComponents).distinct().filter(
                e -> e.equalsIgnoreCase("place") ||
                        e.equalsIgnoreCase("play") ||
                        e.equalsIgnoreCase("draw") ||
                        e.equalsIgnoreCase("disconnect") ||
                        e.equalsIgnoreCase("choose") ||
                        e.equalsIgnoreCase("start") ||
                        e.equalsIgnoreCase("join")
                        // TODO eliminate this part
                        || e.equalsIgnoreCase("send")
        ).findAny().orElse("").toLowerCase();


        switch (keyCommand){
            case "place":
            case "play":
                server.placeCard(new PlaceMessage(command));
                break;
            case "send":
                server.sendMsg(new BaseMessage(command));
                break;
            case "join":
                break;
            case "choose":
                break;
            case "draw":
                break;
            case "disconnect":
                break;
            case "start":
                break;
            default:
                System.out.println("Command not recognised");
                break;
        }

    }
}
