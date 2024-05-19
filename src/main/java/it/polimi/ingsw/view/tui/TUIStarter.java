package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.rmi.RMIClient;

import java.io.PrintWriter;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

import static it.polimi.ingsw.view.tui.ConsoleTextColors.*;

public class TUIStarter {
    private static void printError(PrintWriter out, String msg){
        out.println(RED_TEXT + msg + RESET);
    }

    public static void startTUI() {
        Scanner scanner = new Scanner(System.in);
        PrintWriter out = new PrintWriter(System.out, true);

        String connectionType;
        VirtualClient client;
        do {
            out.print("Enter connection type (TCP o RMI): ");
            connectionType = scanner.nextLine();
            if(connectionType.matches("tcp|TCP")){
                //TODO: instantiate TCP client
                printError(out, "TCP still WIP.");
                connectionType = "";
            } else if (connectionType.matches("rmi|RMI")) {
                //TODO: instantiate RMI client
                try {
                    client = new RMIClient();
                }catch (RemoteException | NotBoundException e){
                    printError(out, "Error while trying to create the RMI client.");
                }
            }
            else printError(out, "Invalid input.");
        }while (!connectionType.matches("tcp|TCP|RMI|rmi"));

        String nickname = "";
        String regex = "[a-zA-Z0-9_]+";
        do {
            out.print("Choose your nickname: ");
            nickname = scanner.nextLine();
            if(nickname.matches(regex)){
                //TODO: attempt client.connect(nickname)
            }
            else printError(out, "Invalid input.");
        }while (!nickname.matches(regex));

    }
}
