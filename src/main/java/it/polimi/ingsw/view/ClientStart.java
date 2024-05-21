package it.polimi.ingsw.view;

import it.polimi.ingsw.view.gui.GUIStarter;
import it.polimi.ingsw.view.tui.scenes.PrintConnectTechUI;

import java.util.Scanner;

public class ClientStart {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the game mode (TUI o GUI):");
        String gameMode = scanner.nextLine();

        if (gameMode.equalsIgnoreCase("GUI")) {
            scanner.close();
            GUIStarter.startGUI();
        } else if (gameMode.equalsIgnoreCase("TUI")) {
            scanner.close();
            new PrintConnectTechUI().display();
        } else {
            System.out.println("Modalità di gioco non valida.");
        }
    }
}