package it.polimi.ingsw.network;

import it.polimi.ingsw.network.rmi.RMIClient;
import it.polimi.ingsw.network.rmi.RMIServer;
import it.polimi.ingsw.network.tcp.server.TCPServerSocket;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Consumer;

import static it.polimi.ingsw.network.rmi.RMI_AddressHelperFunctions.getListOfValidLocalIPs;
import static it.polimi.ingsw.view.tui.ConsoleTextColors.*;

/**
 * This class is the server starter.
 */
public class Server {
    private static final int MAX_PORT = 65535;

    /**
     * Private constructor, as this class is not meant to be instantiated.
     */
    private Server(){}

    /**
     * Main method of the Server. Reads args passed by command line and
     * creates both the RMI and TCP servers in that order, according to the port args. <br>
     * The central server is also created when the RMI server calls CentralServer.getInstance()
     * @param args args contain the following, in this precise order (* is mandatory): <br>
     *         <ul>
     *             <li>
     *                  RMI Server port (0-65535) *
     *             </li>
     *             <li>
     *                  TCP Server port (0-65535) *
     *             </li>
     *             <li>
     *                  Server IP or Hostname (defaults to localhost)
     *             </li>
     *         </ul>
     *              Please note that the RMI and TCP ports must be different from one another. <br><br>
     *              If the "IP" parameter is not passed, the Server will ask for it
     *              and list all available IPs and hostname active on the local machine
     *              (ignoring loopback and local addresses). <br>
     *              If a wrong address is passed as "IP" parameter, the Server will notice and ignore it automatically.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

    //region READ ARGS
        if(args.length < 2){
            System.err.println("Must provide RMI and TCP ports, in that order.");
            System.exit(-1);
        }

        int rmiPort=-1, tcpPort=-1;

        try{
            rmiPort = Integer.parseInt(args[0]);
            tcpPort = Integer.parseInt(args[1]);
            if(rmiPort > MAX_PORT || tcpPort > MAX_PORT){
                System.err.println("Invalid ports provided. They should be within [0 - " + MAX_PORT + "]");
                System.exit(-1);
            }
        }catch (NumberFormatException e){
            System.err.println("Invalid ports provided. They must be numbers");
            System.exit(-1);
        }
    //endregion

    //region SET PROPERTY java.rmi.server.hostname
        System.out.println("Reading Server IPs...");
        List<String> localIPs = getListOfValidLocalIPs();

        String serverIP;
        if(args.length >= 3 && localIPs.contains(args[2])){
            System.out.println("IP/Hostname passed as parameter is valid!");
            serverIP = args[2];
        }
        else{
            if(args.length >= 3)
                System.out.println("IP/Hostname passed as parameter is invalid!");
            System.out.println("Server machine IPs and Hostnames: ");
            System.out.println(YELLOW_TEXT);
            localIPs.forEach(System.out::println);
            System.out.println(RESET);
            System.out.println("Please note: not all these IPs may work. If one doesn't work, try another.\n");
            do{
                System.out.println("Please input one of the options above. Use an IP that is "+ YELLOW_TEXT +"reachable"+ RESET +" by the client.");
                serverIP = scanner.nextLine();
            }while(!localIPs.contains(serverIP));
        }

        System.out.println(YELLOW_TEXT);
        System.out.println("Using " + serverIP + " as Server IP/Hostname");
        System.out.println(RESET);
        System.setProperty("java.rmi.server.hostname", serverIP);
    //endregion

        try{
            new RMIServer(rmiPort); //also creates CentralServer via singleton

            //fix for when a TCP client connects first and then all
            // subsequent RMI clients can't connect to RMIServer
            try {
                new RMIClient(serverIP, rmiPort);
            }catch (NotBoundException e){
                System.err.println("Debug RMIClient creation failed.");
            }
        }catch (RemoteException e){
            System.err.println("Couldn't create RMI Server.");
        }catch (IllegalArgumentException e){
            System.err.println("Invalid RMI port: " + e.getMessage());
        }

        try{
            new TCPServerSocket(tcpPort); //threaded, doesn't stop here
        }catch (IOException e){
            System.err.println("Couldn't create TCP Server.");
        }catch (IllegalArgumentException e){
            //thrown by ServerSocket constructor within TCPServerSocket
            //should never be thrown due to port validation in READ-ARGS
            System.err.println("Invalid TCP port: " + e.getMessage());
        }

    //region DEBUG commands
        String command="";
        while(!command.matches("[qQ]uit")){
            try {
                command = scanner.nextLine();
            }catch (NoSuchElementException crash){
                System.exit(0);
            }
            if(command.matches("[Dd]ebug(\\s+.*)*")){
                String[] cmdArgs = command.split("\\s+");
                if(cmdArgs.length > 1 && cmdArgs[1].equalsIgnoreCase("help")){
                    System.out.println("""
                            Debug <DebugMode> <on|off>
                            
                            There are 3 debug modes:
                            points: multiplies the points x10
                            res: gives 100 resources as the starting card is placed [!ACTIVATE BEFORE PLACING STARTING CARD!]
                            deck
                            emptydeck
                            empty: empties deck faster
                            """);
                } else {
                    if (cmdArgs.length < 3) {
                        System.out.println("""
                                Incorrect debug command:
                                Debug <DebugMode> <on|off>
                                """);
                        continue;
                    }

                    Consumer<Boolean> func = switch (cmdArgs[1].toLowerCase()) {
                        case "points", "point", "p" -> CentralServer::setPointsMode;
                        case "resource", "resources", "res" -> CentralServer::setResourcesMode;
                        case "deck", "empty", "emptydeck" -> CentralServer::setEmptyDeckMode;
                        default -> CentralServer::setDebugMode;
                    };

                    switch (cmdArgs[2].toLowerCase()) {
                        case "on":
                            func.accept(true);
                            break;
                        case "off":
                            func.accept(false);
                            break;
                    }
                }
            }
        }
    //endregion

        System.exit(0);
    }
}
