package it.polimi.ingsw.network;

import it.polimi.ingsw.network.rmi.RMIServer;
import it.polimi.ingsw.network.tcp.server.TCPServerSocket;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class Server {
    private static RMIServer rmiServer = null;
    private static TCPServerSocket tcpServer = null;

    private static boolean isRunningInIDE(){
        try {
            return Server.class.getClassLoader().loadClass("com.intellij.rt.execution.application.AppMainV2") != null;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }
    public static String getBaseIDPath(){
        if(isRunningInIDE())
            return "src/main/java/it/polimi/ingsw/model/resources/";
        else return "";
    }
    public static String getBaseJSONPath(){
        if(isRunningInIDE())
            return "src/main/java/it/polimi/ingsw/model/json/";
        else return "";
    }

    public static void main(String[] args) {
        if(args.length < 2){
            System.err.println("Must provide RMI and TCP ports, in that order.");
            System.exit(-1);
        }
        int rmiPort=-1, tcpPort=-1;
        try{
            rmiPort = Integer.parseInt(args[0]);
            tcpPort = Integer.parseInt(args[1]);
        }catch (NumberFormatException e){
            //FIXME: maybe add limits like port in range [0 - 65,535]
            System.err.println("Invalid ports provided. They must be numbers");
            System.exit(-1);
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
        String quit="";
        while(!quit.matches("[qQ]uit")){
            quit = scanner.nextLine();
        }
        System.exit(0);
    }
}
