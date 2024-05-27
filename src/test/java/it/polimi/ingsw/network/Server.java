package it.polimi.ingsw.network;

import it.polimi.ingsw.network.rmi.RMIClient;
import it.polimi.ingsw.network.rmi.RMIServer;
import it.polimi.ingsw.network.tcp.server.TCPServerSocket;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Server {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try{
            RMIServer rmiServer = new RMIServer(1234);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        try{
            TCPServerSocket tcpServerSocket = new TCPServerSocket(8888);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String line;
        while ((line = scanner.nextLine()) != null){
            if(Pattern.matches("[Qq]uit", line)){
                System.exit(0);
            }
        }
    }
}
