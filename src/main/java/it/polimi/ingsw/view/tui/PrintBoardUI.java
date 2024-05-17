package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.view.model.ViewBoard;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrintBoardUI implements UI_Printer {
    static final int cardSpacing = 4;
    PrintCard printCard;
    ViewBoard board;

    public PrintBoardUI(ViewBoard board){
        this.board = board;
        this.printCard = new PrintCard();
    }

    @Override
    public void printUI(){
        System.out.println("Central Board: \n");

        List<String[]> deckBacks = new LinkedList<>();
        deckBacks.add(printCard.getCardAsStringRows(board.getResourceCardDeck().getTopCard()));
        deckBacks.add(printCard.getCardAsStringRows(board.getGoldCardDeck().getTopCard()));
        deckBacks.add(printCard.getCardAsStringRows(board.getObjectiveCardDeck().getTopCard()));
        for(String[] card : deckBacks){
            // only match resource and gold cards, objective cards are already hidden in PrintCard
            Matcher matcher = Pattern.compile("[RG][0-9]?[0-9]").matcher(card[2]);
            if(matcher.find()){
                String cardID = matcher.group();
                String replacementString = cardID.charAt(0) + printCard.getSpaces(cardID.length()-1);
                card[2] = card[2].replaceFirst("[RG][0-9]?[0-9]", replacementString);
            }
        }

        List<String[]> deckFirstRevealed = new LinkedList<>();
        deckFirstRevealed.add(printCard.getCardAsStringRows(board.getResourceCardDeck().getFirstRevealed()));
        deckFirstRevealed.add(printCard.getCardAsStringRows(board.getGoldCardDeck().getFirstRevealed()));
        deckFirstRevealed.add(printCard.getCardAsStringRows(board.getObjectiveCardDeck().getFirstRevealed()));

        List<String[]> deckSecondRevealed = new LinkedList<>();
        deckSecondRevealed.add(printCard.getCardAsStringRows(board.getResourceCardDeck().getSecondRevealed()));
        deckSecondRevealed.add(printCard.getCardAsStringRows(board.getGoldCardDeck().getSecondRevealed()));
        deckSecondRevealed.add(printCard.getCardAsStringRows(board.getObjectiveCardDeck().getSecondRevealed()));

        printCard.printCardsSideBySide(deckBacks, cardSpacing);
        printCard.printCardsSideBySide(deckFirstRevealed, cardSpacing);
        printCard.printCardsSideBySide(deckSecondRevealed, cardSpacing);
    }

    @Override
    public void moveView(CornerDirection... cornerDirections) {
        printUI();
    }

    @Override
    public void setCenter(int row, int col) {
        printUI();
    }
}
