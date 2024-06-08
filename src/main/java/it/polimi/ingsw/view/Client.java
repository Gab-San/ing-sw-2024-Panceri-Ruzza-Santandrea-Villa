package it.polimi.ingsw.view;

import it.polimi.ingsw.network.CommandPassthrough;
import it.polimi.ingsw.network.rmi.RMIClient;
import it.polimi.ingsw.network.tcp.client.TCPClientSocket;
import it.polimi.ingsw.view.exceptions.DisconnectException;
import it.polimi.ingsw.view.exceptions.TimeoutException;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.model.json.JsonImporter;
import it.polimi.ingsw.view.tui.TUI;

import javax.swing.*;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;

import static it.polimi.ingsw.network.rmi.RMI_AddressHelperFunctions.getListOfValidLocalIPs;
import static it.polimi.ingsw.network.rmi.RMI_AddressHelperFunctions.isLoopbackAddress;
import static it.polimi.ingsw.view.tui.ConsoleTextColors.*;

public class Client {
    public static final int MAX_NICKNAME_LENGTH = 80;
    public static View view = null;
    public static final int MAX_CONNECTION_ATTEMPTS = 5;
    private static final Scanner scanner = new Scanner(System.in);
    private static JsonImporter cardJSONImporter;

//region FUNCTIONS
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
        System.out.println(YELLOW_TEXT + "Client terminated. Press enter to exit..." + RESET);
        System.out.flush();
        scanner.close();
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

//endregion

    /**
     * @param args args contain the following, in any order:
     *             - Server IP or Hostname <br>
     *             - Server port <br>
     *             - The connection tech to be used (TCP/RMI) <br>
     *             If the serverIP is omitted, then localhost will be used
     *             If both a serverIP and a hostname are given, then the IP will be preferred over the hostname
     */
    public static void main(String[] args) {
        String serverIP = null;
        String connectionTech = null;
        int port = -1;

    //region READ-ARGS
        String serverHostname = "localhost";
        String myIP=null;
        for(String arg : args){
            if(arg.matches("\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}")){
                if(serverIP != null)
                    duplicateArgument();
                serverIP = arg;
            }
            else if(arg.matches("[mM][yY][iI][pP]=[^\n ]+")){
                myIP = arg.substring("myIP=".length());
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
            else{
                if(!serverHostname.equals("localhost"))
                    duplicateArgument();
                serverHostname = arg;
            }
        }

        if(connectionTech == null || port <= -1){
            System.err.println("Missing server port or RMI/TCP");
            quitError();
            return; //removes the connectionTech != null warning
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

    //endregion

        cls();
        System.out.println("Searching "+ connectionTech.toUpperCase() +" server at address " + serverIP + ":" + port);

    //region SET RMI CLIENT IP UNLESS PASSED AS PARAMETER
        //This part is only necessary if using RMI on a Client with multiple IPs
        if(connectionTech.equalsIgnoreCase("rmi")) {
            if (!isLoopbackAddress(serverIP)) {
                System.out.print("Processing Client IP...");
                List<String> localIPs = getListOfValidLocalIPs();
                if(!localIPs.contains(myIP)) {
                    if (localIPs.size() > 1) {
                        if (myIP != null) System.out.print(" parameter invalid!");
                        System.out.println("\nYour IPs and Hostnames");
                        System.out.println(YELLOW_TEXT);
                        localIPs.forEach(System.out::println);
                        System.out.println(RESET);
                        do {
                            System.out.println("Please input one of the options above. Use an IP that is " + YELLOW_TEXT + "reachable" + RESET + " by the server.");
                            myIP = scanner.nextLine();
                        } while (!localIPs.contains(myIP));
                        System.out.println();
                    } else if (localIPs.size() == 1) {
                        System.out.println(" located automatically!");
                        myIP = localIPs.get(0);
                    }else{
                        System.out.println("\nThere are no valid network interfaces on this machine. Please connect to a network");
                        quitError();
                    }
                }
                else System.out.println(" parameter valid!");
            }
            else{
                if(myIP == null)
                    myIP = serverIP; // equals loopback address
            }
            if (myIP == null) {
                System.out.println("myIP is null. Fatal error."); // should never happen
                quitError();
                return;
            }
            System.setProperty("java.rmi.server.hostname", myIP);
            System.out.println("Your IP is: " + myIP);
        }
    //endregion

        BlockingQueue<String> inputQueue = initInputQueue();
        while(true) {
            inputQueue.clear();
            CommandPassthrough proxy = null;
            Consumer<ModelUpdater> setClientModelUpdater = null;
        //region CLIENT CREATION AND CONNECTION
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
    //endregion
            try {
                assert setClientModelUpdater != null;
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
                        view = new GUI(proxy, setClientModelUpdater, inputQueue); // proxy always not null at this point
                    }
                    else if (gameMode.equalsIgnoreCase("TUI")) {
                        view = new TUI(proxy, setClientModelUpdater, inputQueue); // proxy always not null at this point
                    } else if (gameMode.toLowerCase().matches("quit|exit|q")) {
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