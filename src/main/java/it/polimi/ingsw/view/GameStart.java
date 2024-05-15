package it.polimi.ingsw.view;

import it.polimi.ingsw.view.tui.TUIStarter;

import java.util.Scanner;

public class GameStart {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the game mode (TUI o GUI):");
        String gameMode = scanner.nextLine();

        if (gameMode.equalsIgnoreCase("GUI")) {
            GUIStarter.startGUI();
        } else if (gameMode.equalsIgnoreCase("TUI")) {
            TUIStarter.startTUI();
        } else {
            System.out.println("Modalità di gioco non valida.");
        }

        scanner.close();
    }
}