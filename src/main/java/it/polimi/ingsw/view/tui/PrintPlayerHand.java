package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.view.model.ViewPlayerHand;
import it.polimi.ingsw.view.model.cards.ViewObjectiveCard;
import it.polimi.ingsw.view.model.cards.ViewPlayCard;

import java.util.LinkedList;
import java.util.List;

public class PrintPlayerHand {
    private final PrintCard printCard;
    private static final int cardSpacing = 4;
    private ViewPlayerHand hand;
    public PrintPlayerHand(ViewPlayerHand hand){
        printCard = new PrintCard();
        this.hand = hand;
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

    public void printCardsInHand(){
        List<String[]> cardsAsStringRows = new LinkedList<>(hand.getCards().stream().map(printCard::getCardAsStringRows).toList());
        printCardsSideBySide(cardsAsStringRows);
    }
    public void printSecretObjectives(){
        List<String[]> cardsAsStringRows = new LinkedList<>(hand.getSecretObjectives().stream().map(printCard::getCardAsStringRows).toList());
        printCardsSideBySide(cardsAsStringRows);
    }
    public void printStartingCard(){
        printCard.printCard(hand.getStartCard());
    }

    private String pad(String str, int desiredLength){
        int diff = desiredLength - str.length();
        return diff > 0 ? str + printCard.getSpaces(diff) : str;
    }
    public void printHand(){
        System.out.println("Nickname: " + hand.getNickname());

        int cardLength = PrintCard.cornerStringAsSpacesLength*2 + PrintCard.cornerRowSpaceCount + cardSpacing;
        String headerLine = "";
        if(hand.getCards().isEmpty())
            System.out.println("No cards in hand.");
        else headerLine += pad("Cards in hand: ", cardLength*hand.getCards().size());
        if(hand.getSecretObjectives().isEmpty())
            System.out.println("No secret objectives in hand.");
        else headerLine += pad("Secret objective: ", cardLength*hand.getSecretObjectives().size());
        if(hand.getStartCard() != null)
            headerLine += pad("Starting card in hand: ", cardLength);

        System.out.println(headerLine);

        List<String[]> cardsAsStringRows = new LinkedList<>(hand.getCards().stream().map(printCard::getCardAsStringRows).toList());
        cardsAsStringRows.addAll(hand.getSecretObjectives().stream().map(printCard::getCardAsStringRows).toList());
        cardsAsStringRows.add(printCard.getCardAsStringRows(hand.getStartCard()));
        printCardsSideBySide(cardsAsStringRows);
        System.out.println();
    }
}
