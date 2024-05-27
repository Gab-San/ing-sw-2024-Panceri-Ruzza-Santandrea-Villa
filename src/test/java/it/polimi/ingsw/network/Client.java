package it.polimi.ingsw.network;

import it.polimi.ingsw.network.rmi.RMIClient;
import it.polimi.ingsw.network.tcp.client.TCPClientSocket;

import java.io.IOException;
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
            if(Pattern.matches("[Qq]uit", line)){
                System.exit(0);
                break;
            }
            try {
                parser.parseCommand(line);
            } catch (RemoteException e) {
                break;
            } catch (IllegalStateException| IllegalArgumentException e){
                System.err.println(e.getMessage());
            }
        }

    }
}
