package it.polimi.ingsw.server.tcp;

import it.polimi.ingsw.server.ModelView;
import it.polimi.ingsw.server.Parser;
import it.polimi.ingsw.server.rmi.RMIClient;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class ClientRun {
    public static void main(String[] args) {
        TCPClient client = null;
        try {
            client = new TCPClient("localhost", 8888);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // TODO correct
        Parser parser = new Parser(client, new ModelView());

        String input = "";
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter command\n> ");
        while (!input.split(" ")[0].equalsIgnoreCase("quit")){
            try{
                input = scanner.nextLine();
                parser.parseCommand(input);
            }catch (RemoteException e){
                System.err.println("Connection lost. Message: " + e.getMessage());
            }catch (IndexOutOfBoundsException e){
                System.err.println("Too few arguments.");
            }catch (IllegalStateException e){
                System.err.println("Cannot execute command. Message: " + e.getMessage());
            }catch (IllegalArgumentException e){
                System.err.println("Command invalid. Message: " + e.getMessage());
            }
        }
        System.exit(0);
    }
}
