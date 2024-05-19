package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.Point;
import it.polimi.ingsw.view.model.ViewBoard;
import it.polimi.ingsw.view.model.ViewOpponentHand;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static it.polimi.ingsw.view.tui.ConsoleBackgroundColors.*;
import static it.polimi.ingsw.view.tui.ConsoleTextColors.GREEN_TEXT;
import static it.polimi.ingsw.view.tui.ConsoleTextColors.RED_TEXT;

public class PrintBoardUI implements Scene {
    static final int cardSpacing = 4;
    PrintCard printCard;
    ViewBoard board;

    public PrintBoardUI(ViewBoard board){
        this.board = board;
        this.printCard = new PrintCard();
    }

    @Override
    public void display(){
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

        String myColor = getColorFromEnum(board.getPlayerHand().getColor());
        System.out.print("Player list:\t" + myColor + " Me (" + board.getPlayerHand().getNickname() + ") " + RESET);
        for(ViewOpponentHand opponent : board.getOpponents()){
            System.out.print("\t\t" + getColorFromEnum(opponent.getColor()) + " " + opponent.getNickname() + " " + RESET);
        }
        System.out.println();
    }

    @Override
    public void moveView(CornerDirection... cornerDirections) { display(); }
    @Override
    public void setCenter(int row, int col) { display(); }
    @Override
    public void setCenter(Point center) { display(); }

}
