package it.polimi.ingsw.view.tui.scenes;

import it.polimi.ingsw.view.model.ViewBoard;
import it.polimi.ingsw.view.model.ViewHand;
import it.polimi.ingsw.view.model.ViewOpponentHand;
import it.polimi.ingsw.view.tui.TUI_Scene;
import it.polimi.ingsw.view.tui.printers.PrintCard;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static it.polimi.ingsw.view.tui.ConsoleBackgroundColors.*;
import static it.polimi.ingsw.view.tui.ConsoleTextColors.*;
import static it.polimi.ingsw.view.tui.ConsoleTextColors.RESET;
import static it.polimi.ingsw.view.tui.ConsoleColorsCombiner.combine;

/**
 * The Board UI scene printer for the TUI
 */
public class PrintBoardUI extends TUI_Scene {
    static final int cardSpacing = 4;
    private final PrintCard printCard;
    private final ViewBoard board;

    /**
     * Constructs the Board UI scene
     * @param board ViewBoard of the game to be displayed
     */
    public PrintBoardUI(ViewBoard board){
        super();
        this.board = board;
        this.printCard = new PrintCard();
    }

    @Override
    public void print(){
        out.print("Scoreboard:\t");
        if(board.isEndgame())
            out.print(YELLOW_BRIGHT_TEXT + "[ENDGAME]" + RESET);
        else out.print("score <- " + CYAN_BRIGHT + " " + RESET);
        out.println("\n");
        for (int i = 0; i <= 29; i++) {
            StringBuilder playersOnI = new StringBuilder();
            List<ViewHand> hands = board.getAllPlayerHands();
            for(ViewHand hand : hands){
                if(hand.getColor() != null && board.getScore(hand.getNickname()) == i){
                    String playerMarker = getColorFromEnum(hand.getColor()) + " " + RESET;
                    playersOnI.append(playerMarker).append(" ");
                }
            }

            if(i == ViewBoard.ENDGAME_SCORE) out.print(YELLOW_BRIGHT_TEXT + i + RESET);
            else out.print(i);
            out.print(" " + playersOnI + RESET);
        }

        out.println("\n\nCentral Board.    " + board.getGamePhase() + "    Turn "+ board.getCurrentTurn() +"\n");

        List<String[]> deckBacks = new LinkedList<>();
        deckBacks.add(printCard.cutAllCornersIfEmpty(printCard.getCardAsStringRows(board.getResourceCardDeck().getTopCard())));
        deckBacks.add(printCard.cutAllCornersIfEmpty(printCard.getCardAsStringRows(board.getGoldCardDeck().getTopCard())));
        deckBacks.add(printCard.cutAllCornersIfEmpty(printCard.getCardAsStringRows(board.getObjectiveCardDeck().getTopCard(), true)));
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
        deckFirstRevealed.add(printCard.cutAllCornersIfEmpty(printCard.getCardAsStringRows(board.getResourceCardDeck().getFirstRevealed())));
        deckFirstRevealed.add(printCard.cutAllCornersIfEmpty(printCard.getCardAsStringRows(board.getGoldCardDeck().getFirstRevealed())));
        deckFirstRevealed.add(printCard.cutAllCornersIfEmpty(printCard.getCardAsStringRows(board.getObjectiveCardDeck().getFirstRevealed())));

        List<String[]> deckSecondRevealed = new LinkedList<>();
        deckSecondRevealed.add(printCard.cutAllCornersIfEmpty(printCard.getCardAsStringRows(board.getResourceCardDeck().getSecondRevealed())));
        deckSecondRevealed.add(printCard.cutAllCornersIfEmpty(printCard.getCardAsStringRows(board.getGoldCardDeck().getSecondRevealed())));
        deckSecondRevealed.add(printCard.cutAllCornersIfEmpty(printCard.getCardAsStringRows(board.getObjectiveCardDeck().getSecondRevealed())));

        printCard.printCardsSideBySide(deckBacks, cardSpacing);
        printCard.printCardsSideBySide(deckFirstRevealed, cardSpacing);
        printCard.printCardsSideBySide(deckSecondRevealed, cardSpacing);

        String myColor = getColorFromEnum(board.getPlayerHand().getColor());
        if(board.getPlayerHand().getTurn() == 1)
            myColor = combine(BLACK_TEXT, myColor);
        out.print("Player list:\t" + myColor + " Me (" + board.getPlayerHand().getNickname() + ") " + RESET);
        for(ViewOpponentHand opponent : board.getOpponents()){
            String colorBG = getColorFromEnum(opponent.getColor());
            String colorFG = opponent.getTurn()==1 ? BLACK_TEXT : "";
            String nickname = opponent.isConnected() ? opponent.getNickname() : opponent.getNickname() + " (OFFLINE)";
            out.print("\t\t" +  combine(colorFG, colorBG) + " " + nickname + " " + RESET);
        }
        out.println();
    }
}
