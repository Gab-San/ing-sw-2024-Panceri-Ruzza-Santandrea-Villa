package it.polimi.ingsw.view;

import java.io.PrintWriter;
import java.util.Scanner;

public class TUIStarter {
    public static void startTUI() {
        Scanner scanner = new Scanner(System.in);
        PrintWriter out = new PrintWriter(System.out, true);
        PrintCard printCard = new PrintCard();

        out.println("Enter connection type (TCP o RMI):");
        String connectionType = scanner.nextLine();

        out.println("Choose your nickname:");
        String nickname = scanner.nextLine();

        out.println("Enter card ID (e.g., G0, G33):");
        String cardId = scanner.nextLine();

        out.println("Enter card color (red, purple, green, cyan):");
        String color = scanner.nextLine();

        // Stampa la carta nel terminale con il colore scelto
        //printCard.printCard(cardId, color);
    }
}
