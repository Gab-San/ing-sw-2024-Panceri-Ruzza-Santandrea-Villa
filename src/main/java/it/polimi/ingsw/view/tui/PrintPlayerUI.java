package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.view.model.ViewPlayerHand;
import it.polimi.ingsw.view.model.cards.ViewObjectiveCard;
import it.polimi.ingsw.view.model.cards.ViewPlayCard;

import java.util.LinkedList;
import java.util.List;

public class PrintPlayerUI {
    private final PrintCard printCard;
    private final PrintPlayArea printPlayArea;
    private static final int cardSpacing = 4;

    public PrintPlayerUI(){
        printCard = new PrintCard();
        printPlayArea = new PrintPlayArea();
    }

    private void printCardsSideBySide(List<String[]> cardsAsStringRows){
        if(cardsAsStringRows.isEmpty()) return;

        String spacing = printCard.getSpaces(cardSpacing);
        for (int i = 0; i < cardsAsStringRows.get(0).length; i++) {
            for (String[] cardAsRows : cardsAsStringRows){
                System.out.print(cardAsRows[i] + spacing);
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }

    public void printCardsInHand(ViewPlayerHand hand){
        List<String[]> cardsAsStringRows = new LinkedList<>();
        for(ViewPlayCard c : hand.getCards()){
            cardsAsStringRows.add(printCard.getCardAsStringRows(c));
        }
        printCardsSideBySide(cardsAsStringRows);
    }
    public void printSecretObjectives(ViewPlayerHand hand){
        List<String[]> cardsAsStringRows = new LinkedList<>();
        for(ViewObjectiveCard c : hand.getSecretObjectives()){
            cardsAsStringRows.add(printCard.getCardAsStringRows(c));
        }
        printCardsSideBySide(cardsAsStringRows);
    }

    public void printHand(ViewPlayerHand hand){
        System.out.println("Nickname: " + hand.getNickname());

        System.out.println("Secret objective: ");
        printSecretObjectives(hand);

        if(hand.getStartCard() != null){
            System.out.println("Starting card in hand: ");
            printCard.printCard(hand.getStartCard());
        }

        System.out.println("Cards in hand: ");
        printCardsInHand(hand);
    }
}
