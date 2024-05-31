package it.polimi.ingsw.view;

import it.polimi.ingsw.network.CommandPassthrough;
import it.polimi.ingsw.network.rmi.RMIClient;
import it.polimi.ingsw.network.tcp.client.TCPClientSocket;
import it.polimi.ingsw.view.exceptions.DisconnectException;
import it.polimi.ingsw.view.exceptions.TimeoutException;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.model.json.JsonImporter;
import it.polimi.ingsw.view.tui.TUI;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.function.Consumer;

import static it.polimi.ingsw.view.tui.ConsoleTextColors.*;

public class Client {
    public static final int MAX_NICKNAME_LENGTH = 80;
    public static View view = null;
    public static final int MAX_CONNECTION_ATTEMPTS = 5;
    private static final Scanner scanner = new Scanner(System.in);
    private static JsonImporter cardJSONImporter;
    private static String serverIP;
    private static int port;
    private static String connectionTech;

    public static boolean isRunningInIDE(){
        try {
            return Client.class.getClassLoader().loadClass("com.intellij.rt.execution.application.AppMainV2") != null;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }
    public static void cls(){
        synchronized (System.out) {
            //TODO: delete \n screen before release (needed for IDE console cls)
            if (isRunningInIDE()) System.out.print("\n".repeat(50));
            else {
                System.out.print("\033[H\033[2J");
                //FIXME: [Ale] choose which cls to use
                //source: https://stackoverflow.com/questions/2979383/how-to-clear-the-console-using-java
                try {
                    if (System.getProperty("os.name").contains("Windows"))
                        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                    else Runtime.getRuntime().exec("clear");
                } catch (Exception ignored) {
                }
            }
            System.out.flush();
        }
    }

    public static JsonImporter getCardJSONImporter(){
        return cardJSONImporter;
    }
    private static void duplicateArgument(){
        System.err.println("Duplicate argument detected. Closing.");
        quitError();
    }
    private static void quitError(){
        scanner.close();
        System.out.print(YELLOW_TEXT + "Press enter to exit..." + RESET);
        System.exit(-1);
    }
    private static BlockingQueue<String> initInputQueue(){
        BlockingQueue<String> inputQueue = new LinkedBlockingDeque<>();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Runnable inputReader = () -> {
            try {
                while(true) {
                        String str;
                        synchronized (scanner) {
                            str = scanner.nextLine();
                        }
                        inputQueue.add(str);
                }
            }catch (NoSuchElementException ignored){}
        };
        executor.submit(inputReader);
        return inputQueue;
    }

    /**
     * @param args args contain the following, in any order:
     *             - Server IP or Hostname <br>
     *             - Server port <br>
     *             - The connection tech to be used (TCP/RMI) <br>
     *             If the serverIP is omitted, then localhost will be used
     *             If both a serverIP and a hostname are given, then the IP will be preferred over the hostname
     */
    public static void main(String[] args) {
        serverIP = null; connectionTech = null; port = -1;
        boolean verbose = false;

        String serverHostname = "localhost";
        for(String arg : args){
            if(arg.matches("\\d+.\\d+.\\d+.\\d+")){
                if(serverIP != null)
                    duplicateArgument();
                serverIP = arg;
            }
            else if(arg.toLowerCase().matches("rmi|tcp")) {
                if(connectionTech != null)
                    duplicateArgument();
                connectionTech = arg;
            }
            else if(arg.matches("\\d+")) {
                if(port >= 0)
                    duplicateArgument();
                port = Integer.parseInt(arg);
            }
            else if(arg.equalsIgnoreCase("-v")) {
                if(verbose)
                    duplicateArgument();
                verbose = true;
            }
            else{
                if(!serverHostname.equals("localhost"))
                    duplicateArgument();
                serverHostname = arg;
            }
        }
        if(connectionTech == null || port <= -1){
            System.err.println("Missing server port or RMI/TCP");
            quitError();
        }
        if(serverIP == null) //if no IP was found among args
            serverIP = serverHostname;

        try {
            cardJSONImporter = new JsonImporter();
        } catch (IOException e) {
            System.err.println("Error while reading card JSON from disk.");
            System.err.println(e.getMessage());
            quitError();
        }
        cls();
        System.out.println("Searching "+ connectionTech.toUpperCase() +" server at address " + serverIP + ":" + port);

        BlockingQueue<String> inputQueue = initInputQueue();
        while(true) {
            inputQueue.clear();
            CommandPassthrough proxy = null;
            Consumer<ModelUpdater> setClientModelUpdater = null;
            for (int i = 0; i <= MAX_CONNECTION_ATTEMPTS; i++) {
                try {
                    if (connectionTech.equalsIgnoreCase("tcp")) {
                        TCPClientSocket tcpClient = new TCPClientSocket(serverIP, port);
                        proxy = tcpClient.getProxy();
                        setClientModelUpdater = tcpClient::setModelUpdater;
                        break;
                    } else if (connectionTech.equalsIgnoreCase("rmi")) {
                        RMIClient rmiClient = new RMIClient(serverIP, port);
                        proxy = rmiClient.getProxy();
                        setClientModelUpdater = rmiClient::setModelUpdater;
                        break;
                    } else {
                        System.err.println("Wrong connection technology passed as parameter. Must be TCP/RMI");
                        quitError();
                    }
                } catch (IOException | NotBoundException e) {
                    if(i < MAX_CONNECTION_ATTEMPTS){
                        System.err.println("Couldn't locate server. Trying again... #"+i);
                    }
                }

                try{
                    Thread.sleep(500);
                }catch (InterruptedException e){
                    System.err.println("Couldn't locate server. Closing client.");
                    quitError();
                }
            }

            if(proxy == null){
                System.err.println("Couldn't locate server. Closing client.");
                quitError();
            }
            try {
                System.out.println(GREEN_TEXT + "Server located successfully!" + RESET);
                System.out.println("If the above text is not green, TUI is not supported on the current console.");
                while (view == null) {
                    System.out.println("Enter the game mode (TUI or GUI). Please use fullscreen (Alt+Enter) if selecting TUI.");
                    System.out.print("> ");

                    String gameMode = "";
                    try {
                        gameMode = inputQueue.take();
                    }catch (InterruptedException ignored){}

                    if (gameMode.equalsIgnoreCase("GUI")) {
                        view = new GUI(proxy, setClientModelUpdater); // proxy always not null at this point
                    }
                    else if (gameMode.equalsIgnoreCase("TUI")) {
                        view = new TUI(proxy, setClientModelUpdater, inputQueue, verbose); // proxy always not null at this point
                    } else if (gameMode.toLowerCase().matches("quit|exit|q")) {
                        System.out.println(YELLOW_TEXT + "Client terminated." + RESET);
                        quitError();
                    } else {
                        System.out.println(RED_TEXT + "Invalid input." + RESET);
                        view = null;
                    }
                    if(view != null) view.run();
                }
            } catch (RemoteException e) {
                cls();
                view = null;
                System.out.println(RED_TEXT + "Server connection lost. Trying to recover..." + RESET);
            }
            catch (DisconnectException e){
                cls();
                view = null;
                System.out.println(GREEN_TEXT + "Disconnection succeeded. Returning to main menu." + RESET);
            }
            catch (TimeoutException e){
                cls();
                view = null;
                System.out.println(RED_TEXT + "Server disconnected you for inactivity. Returning to main menu." + RESET);
            }
        }

    }
}