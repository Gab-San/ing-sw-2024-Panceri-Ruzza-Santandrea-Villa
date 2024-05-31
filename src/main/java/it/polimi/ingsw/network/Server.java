package it.polimi.ingsw.network;

import it.polimi.ingsw.network.rmi.RMIServer;
import it.polimi.ingsw.network.tcp.server.TCPServerSocket;

import java.io.IOException;
import java.net.*;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import static it.polimi.ingsw.view.tui.ConsoleTextColors.*;

public class Server {
    private static RMIServer rmiServer = null;
    private static TCPServerSocket tcpServer = null;
    private static final int MAX_PORT = 65535;

    public static void main(String[] args) {
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
        List<String> localIPs = new LinkedList<>();
        try {
            for (Iterator<NetworkInterface> it = NetworkInterface.getNetworkInterfaces().asIterator(); it.hasNext(); ) {
                NetworkInterface net = it.next();
                for (Iterator<InetAddress> iter = net.getInetAddresses().asIterator(); iter.hasNext(); ) {
                    InetAddress address = iter.next();
                    boolean isRelevantAddress = !address.isLoopbackAddress() && address instanceof Inet4Address;
                    String hostName = isRelevantAddress ? address.getHostName() : "";
                    boolean otherFilters = !hostName.contains("mshome.net");
                    if(isRelevantAddress && otherFilters) {
                        localIPs.add(address.getHostAddress());
                        localIPs.add(hostName);
                    }
                }
            }
        }catch (SocketException ignored){}

        if(args.length >= 3 && localIPs.contains(args[2])){
            System.out.println("IP/Hostname passed as parameter is valid!");
            System.out.println(YELLOW_TEXT);
            System.out.println("Using " + args[2] + " as Server IP/Hostname");
            System.out.println(RESET);
            System.setProperty("java.rmi.server.hostname", args[2]);
        }
        else{
            if(args.length >= 3)
                System.out.println("IP/Hostname passed as parameter is invalid!");
            System.out.println("Server machine IPs and Hostnames: ");
            System.out.println(YELLOW_TEXT);
            localIPs.forEach(System.out::println);
            System.out.println(RESET);
            System.out.println("Please note: not all these IPs may work. If one doesn't work, try another.\n");
        }

        try{
            rmiServer = new RMIServer(rmiPort); //also creates CentralServer via singleton
        }catch (RemoteException e){
            System.err.println("Couldn't create RMI Server.");
        }catch (IllegalArgumentException e){
            System.err.println("Invalid TCP port: " + e.getMessage());
        }

        try{
            tcpServer = new TCPServerSocket(tcpPort); //threaded, doesn't stop here
        }catch (IOException e){
            System.err.println("Couldn't create TCP Server.");
        }catch (IllegalArgumentException e){
            //thrown by ServerSocket constructor within TCPServerSocket
            System.err.println("Invalid TCP port: " + e.getMessage());
        }

        Scanner scanner = new Scanner(System.in);
        String command="";
        while(!command.matches("[qQ]uit")){
            command = scanner.nextLine();
            if(command.matches("[Dd]ebug(\\s+.*)*")){
                String[] cmdArgs = command.split("\\s+");
                if(cmdArgs.length < 2){
                    System.err.println("MISSING DEBUG SWITCH (ON|OFF)");
                    continue;
                }

                switch(cmdArgs[1].toLowerCase()){
                    case "on":
                        CentralServer.setDebugMode(true);
                        break;
                    case "off":
                        CentralServer.setDebugMode(false);
                        break;
                }
            }
        }
        System.exit(0);
    }
}
