package it.polimi.ingsw.server.rmi;

import it.polimi.ingsw.server.ModelView;
import it.polimi.ingsw.server.Parser;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class RMIClientTest {
    public static void main(String[] args) throws RemoteException, NotBoundException {
        System.err.println("CLIENT TO BE RUN ONLY FOR TEST PURPOSES");
        if(args.length < 1) {
            args = new String[1];
            args[0] = "localhost";
        }

        RMIClient client = new RMIClient(args[0]);
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