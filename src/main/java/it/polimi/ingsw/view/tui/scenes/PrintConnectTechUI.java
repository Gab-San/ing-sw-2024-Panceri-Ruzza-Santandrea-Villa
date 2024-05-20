package it.polimi.ingsw.view.tui.scenes;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.Point;
import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.rmi.RMIClient;
import it.polimi.ingsw.view.Scene;

import java.io.PrintWriter;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

import static it.polimi.ingsw.view.tui.ConsoleTextColors.*;

public class PrintConnectTechUI implements Scene {
    PrintWriter out;
    Scanner scanner;

    public PrintConnectTechUI(){
        scanner = new Scanner(System.in);
        out = new PrintWriter(System.out, true);
    }
    @Override
    public void displayError(String msg){
        out.println(RED_TEXT + msg + RESET);
    }

    @Override
    public void display() {

        String connectionType;
        VirtualClient client;
        do {
            out.print("Enter connection type (TCP o RMI): ");
            connectionType = scanner.nextLine();
            if(connectionType.matches("tcp|TCP")){
                //TODO: instantiate TCP client
                displayError("TCP still WIP.");
                connectionType = "";
            } else if (connectionType.matches("rmi|RMI")) {
                //TODO: instantiate RMI client
                try {
                    client = new RMIClient();
                }catch (RemoteException | NotBoundException e){
                    displayError("Error while trying to create the RMI client.");
                }
            }
            else displayError("Invalid input.");
        }while (!connectionType.matches("tcp|TCP|RMI|rmi"));

        scanner.close();
        new PrintNicknameSelectUI().display();
    }

    @Override
    public void moveView(CornerDirection... cornerDirections) {
        display();
    }
    @Override
    public void setCenter(int row, int col) {
        display();
    }
    @Override
    public void setCenter(Point center) {
        display();
    }
}
