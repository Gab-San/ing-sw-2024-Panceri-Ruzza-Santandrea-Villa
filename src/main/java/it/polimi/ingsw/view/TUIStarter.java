package it.polimi.ingsw.view;

import java.util.Scanner;

public class TUIStarter {
    public static void startTUI() {
        Scanner scanner = new Scanner(System.in);

        // Chiedi all'utente il tipo di connessione
        System.out.println("Enter connection type (TCP o RMI):");
        String connectionType = scanner.nextLine();

        // Chiedi all'utente di scegliere un nickname
        System.out.println("Choose your nickname:");
        String nickname = scanner.nextLine();

        // Chiedi all'utente se vuole partecipare a una partita esistente o iniziarne una nuova
        System.out.println("Enter 'new ID_game':");
        String gameId = scanner.nextLine();

        if (gameId.equalsIgnoreCase("NEW")) {
            // Inizia una nuova partita
            System.out.println("Nuova partita iniziata...");
            // Avvia la schermata di gioco
            //GameCLI.startGame(connectionType, nickname, gameId);
        } else {
            // Partecipa a una partita esistente
            System.out.println("Partecipazione alla partita con ID " + gameId + "...");
            // Avvia la schermata di gioco
            //GameCLI.startGame(connectionType, nickname, gameId);
        }

        scanner.close();
    }
}