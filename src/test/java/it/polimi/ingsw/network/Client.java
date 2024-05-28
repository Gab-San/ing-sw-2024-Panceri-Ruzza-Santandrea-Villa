package it.polimi.ingsw.network;

import it.polimi.ingsw.network.rmi.RMIClient;
import it.polimi.ingsw.network.tcp.client.TCPClientSocket;

import java.io.IOException;
import java.net.StandardSocketOptions;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Client {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CommandPassthrough proxy;
        do {
            System.out.println("RMI OR TCP?");

            String line = scanner.nextLine();
            if (line.equalsIgnoreCase("rmi")) {
                try {
                    RMIClient rmiClient = new RMIClient(1234);
                    proxy = rmiClient.getProxy();
                } catch (RemoteException | NotBoundException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    TCPClientSocket clientSocket = new TCPClientSocket(8888);
                    proxy = clientSocket.getProxy();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } while (proxy == null);

        Parser parser = new Parser(proxy, new ModelView());

        String line;
        while((line = scanner.nextLine()) != null){
            System.out.println("WRITE COMMAND: ");
            if(Pattern.matches("[Qq]uit", line)){
                System.out.print("EXITING");
                System.exit(1);
            }

            try {
                parser.parseCommand(line);
            } catch (RemoteException e) {
                System.out.println("YOU ARE DISCONNECTED: RECONNECT...");
            } catch (IllegalStateException| IllegalArgumentException e){
                System.err.println(e.getMessage());
            }
        }
        System.err.println("EXITING WITH ERROR");
        System.exit(-1);
    }
}
