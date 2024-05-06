import it.polimi.ingsw.model.json.deserializers.GoldCardJSON;

import java.io.PrintWriter;
import java.util.Scanner;

public class TUIStarter {
    public static void startTUI() {
        Scanner scanner = new Scanner(System.in);
        PrintWriter out = new PrintWriter(System.out, true);

        out.println("Enter connection type (TCP o RMI):");
        String connectionType = scanner.nextLine();

        out.println("Choose your nickname:");
        String nickname = scanner.nextLine();

        out.println("Enter card ID (e.g., G0, G33):");
        String cardId = scanner.nextLine();

        GoldCardJSON card = retrieveCardFromModel(cardId);

        // Disegna la carta specificata
        DrawGoldCard drawGoldCard = new DrawGoldCard();
        drawGoldCard.drawCard(card, out, true); // Disegna il fronte della carta

        scanner.close();
    }

    private static GoldCardJSON retrieveCardFromModel(String cardId) {
        GoldCardJSON card = new GoldCardJSON();
        card.setCardId(cardId);
        return card;
    }
}
