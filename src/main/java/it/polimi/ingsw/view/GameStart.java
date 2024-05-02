package it.polimi.ingsw.view;

import java.util.Scanner;

public class GameStart {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Chiedi all'utente la modalità di gioco
        System.out.println("Enter the game mode (TUI o GUI):");
        String gameMode = scanner.nextLine();

        if (gameMode.equalsIgnoreCase("GUI")) {
            GUIStarter.startGUI(); // Avvia la schermata grafica
        } else if (gameMode.equalsIgnoreCase("TUI")) {
            TUIStarter.startTUI();// Avvia la schermata testuale
        } else {
            System.out.println("Modalità di gioco non valida.");
        }

        scanner.close();
    }
}