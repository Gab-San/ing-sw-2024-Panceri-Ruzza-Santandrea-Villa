import it.polimi.ingsw.model.json.deserializers.GoldCardJSON;

import java.io.PrintWriter;
import java.util.Scanner;

public class TUIStarter {
    public static void startTUI() {
        Scanner scanner = new Scanner(System.in);
        PrintWriter out = new PrintWriter(System.out, true);

        // Chiedi all'utente il tipo di connessione
        out.println("Enter connection type (TCP o RMI):");
        String connectionType = scanner.nextLine();

        // Chiedi all'utente di scegliere un nickname
        out.println("Choose your nickname:");
        String nickname = scanner.nextLine();

        // Chiedi all'utente di inserire l'ID della carta da disegnare
        out.println("Enter card ID (e.g., G0, G33):");
        String cardId = scanner.nextLine();

        // Crea un oggetto GoldCardJSON per la carta specificata
        GoldCardJSON card = retrieveCardFromModel(cardId);

        // Disegna la carta specificata
        DrawGoldCard drawGoldCard = new DrawGoldCard();
        drawGoldCard.drawCard(card, out, true); // Disegna il fronte della carta

        scanner.close();
    }

    // Metodo per recuperare un'istanza di GoldCardJSON dal modello dei dati
    private static GoldCardJSON retrieveCardFromModel(String cardId) {
        // Implementa la logica per recuperare l'oggetto GoldCardJSON dal tuo modello dei dati
        // Restituisce un'istanza fittizia in questo esempio
        GoldCardJSON card = new GoldCardJSON();
        card.setCardId(cardId);
        return card;
    }
}
