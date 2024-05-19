package it.polimi.ingsw.view.tui.printers;

import it.polimi.ingsw.view.model.ViewHand;

import static it.polimi.ingsw.view.tui.ConsoleBackgroundColors.RESET;
import static it.polimi.ingsw.view.tui.ConsoleBackgroundColors.getColorFromEnum;

import java.util.LinkedList;
import java.util.List;

public class PrintHand {
    private final PrintCard printCard;
    private static final int cardSpacing = 4;
    private final ViewHand hand;
    public PrintHand(ViewHand hand){
        printCard = new PrintCard();
        this.hand = hand;
    }

    private String pad(String str, int desiredLength){
        int diff = desiredLength - str.length();
        return diff > 0 ? str + printCard.getSpaces(diff) : str;
    }

    public void printHand(){
        printHand(false);
    }
    public void printHand(boolean hideID){
        System.out.print("Nickname: " + hand.getNickname());
        if(hand.getTurn() > 0)
            System.out.print("\t- Turn: " + hand.getTurn());
        if(hand.getColor() != null){
            System.out.print("\t- Color: " + getColorFromEnum(hand.getColor()) + " " + hand.getColor() + " " + RESET);
        }

        System.out.println();

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

        List<String[]> cardsAsStringRows = new LinkedList<>(hand.getCards().stream().map(c->printCard.getCardAsStringRows(c, hideID)).toList());
        List<String[]> cardsInHand = hand.getSecretObjectives().stream().map(c->printCard.getCardAsStringRows(c, hideID)).toList();
        cardsAsStringRows.addAll(cardsInHand);
        if(hand.getStartCard() != null)
            cardsAsStringRows.add(printCard.getCardAsStringRows(hand.getStartCard(), hideID));
        printCard.printCardsSideBySide(cardsAsStringRows, cardSpacing);
        System.out.println();
    }
}
