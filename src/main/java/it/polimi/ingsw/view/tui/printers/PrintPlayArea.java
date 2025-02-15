package it.polimi.ingsw.view.tui.printers;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.view.model.ViewPlayArea;
import it.polimi.ingsw.view.model.cards.ViewPlaceableCard;
import it.polimi.ingsw.view.tui.ConsoleBackgroundColors;
import it.polimi.ingsw.view.tui.ConsoleColorsCombiner;

import java.util.HashSet;
import java.util.Set;

import static it.polimi.ingsw.CornerDirection.*;
import static it.polimi.ingsw.view.tui.ConsoleBackgroundColors.WHITE;
import static it.polimi.ingsw.view.tui.ConsoleTextColors.*;

/**
 * This printer is used to visualise any player's PlayArea.
 */
public class PrintPlayArea {
    private final PrintCard printCard;
    private final ViewPlayArea playArea;
    private final String freeCornerColor = ConsoleColorsCombiner.combine(PURPLE_TEXT, WHITE);

    /**
     * Constructs the ViewPlayArea printer
     * @param playArea the playArea of this printer
     */
    public PrintPlayArea(ViewPlayArea playArea){
        printCard = new PrintCard();
        this.playArea = playArea;
    }

    /**
     * Cuts the string corresponding to a card's corner
     * @param row the row from which to cut the corner
     * @param dir the corner direction (TL/BL and TR/BR are treated as left and right)
     * @return the string corresponding to row without the card's corner in given direction
     */
    private String cutCorner(String row, CornerDirection dir, Set<CornerDirection> fillCorners){
        String spaces = (fillCorners.contains(dir) ? printCard.getSpaces(PrintCard.cornerStringAsSpacesLength) : "");
        return switch (dir){
            case TL, BL -> spaces + row.substring(PrintCard.cornerStringLength);
            case TR, BR -> row.substring(0, row.length() - PrintCard.cornerStringLength) + spaces;
        };
    }

    /**
     * Multiple successive recolorings of the same corner are not allowed
     * and may produce unwanted behaviour.
     * @param row the card's String[5] corner row to recolor. Only pass a default row 0 or 4.
     * @param dir the corner direction (TL/BL and TR/BR are treated as left and right)
     * @param color the new color of the corner
     * @return the row with a re-formatted corner, replacing its old color (only if it was the standard color)
     */
    private String recolorCorner(String row, CornerDirection dir, String color) {
        int rightCorner = PrintCard.cornerStringLength;
        int leftCorner = row.length() - PrintCard.cornerStringLength;
        int orgColorLen = PrintCard.cornerColor.length();
        return switch (dir){
            case TL, BL -> color + row.substring(orgColorLen, rightCorner) + row.substring(rightCorner);
            case TR, BR -> row.substring(0, leftCorner) + color + row.substring(leftCorner+orgColorLen);
        };
    }

    /**
     * @param card the ViewPlaceableCard to transform to String[5] representation
     * @param fillCorners corner directions to fill with spaces
     * @return the String[5] representation of card, cutting the non-visible corners and filling
     * the corners whose direction is contained in fillCorners
     */
    private String[] getCardAsStringRows(ViewPlaceableCard card, Set<CornerDirection> fillCorners){
        String[] cardAsStringRows = printCard.getCardAsStringRows(card);

        for (CornerDirection dir : CornerDirection.values()) {
            if (card == null || !card.getCorner(dir).isVisible()) {
                switch (dir) {
                    case TR, TL:
                        cardAsStringRows[0] = cutCorner(cardAsStringRows[0], dir, fillCorners);
                        break;
                    case BR, BL:
                        cardAsStringRows[4] = cutCorner(cardAsStringRows[4], dir, fillCorners);
                        break;
                }
            }
            else // card != null && corner visible
                // getCardAt() == null condition is checked first for efficiency
                if(playArea.getCardAt(card.getPosition().move(dir)) == null && playArea.getFreeCorners().contains(card.getCorner(dir))){
                    switch (dir) {
                        case TR, TL:
                            cardAsStringRows[0] = recolorCorner(cardAsStringRows[0], dir, freeCornerColor);
                            break;
                        case BR, BL:
                            cardAsStringRows[4] = recolorCorner(cardAsStringRows[4], dir, freeCornerColor);
                            break;
                    }
                }
        }
        return cardAsStringRows;
    }

    /**
     * Short alias for System.out.print(String str)
     * @param str string to be printed
     */
    private void print(String str){
        System.out.print(str);
    }

    /**
     * Short alias for System.out.print("\n")
     */
    private void endl(){ System.out.print("\n"); }

    // prints according to following schema:
    /*
topLeftCard      topCard       topRightCard
          TLCard         TRCard
  leftCard       midCard       rightCard
          BLCard         BRCard
botLeftCard      botCard      botRightCard
     */
    /**
     * Prints a 5-cards-wide square view of this printer's playArea centered on the given point. <br><br>
     *
     * The outside corners of cards on the edges of the view that are
     * covered by another card (not in view) are not shown to indicate
     * the presence of another card in that outside position.
     * @param center position of the card to be printed as the center
     *               (midCard in the above schema)
     */
    public void printPlayArea(GamePoint center){
        final Set<CornerDirection> fillCorners = new HashSet<>();

    //region Import cards
        //import the necessary cards, reading from playArea
        ViewPlaceableCard topCard = playArea.getCardAt(center.move(TL, TR));
        ViewPlaceableCard topLeftCard = playArea.getCardAt(center.move(TL, TL));
        ViewPlaceableCard topRightCard = playArea.getCardAt(center.move(TR, TR));

        ViewPlaceableCard TLCard = playArea.getCardAt(center.move(TL));
        ViewPlaceableCard TRCard = playArea.getCardAt(center.move(TR));

        ViewPlaceableCard leftCard = playArea.getCardAt(center.move(TL, BL));
        ViewPlaceableCard midCard = playArea.getCardAt(center);
        ViewPlaceableCard rightCard = playArea.getCardAt(center.move(TR, BR));

        ViewPlaceableCard BLCard = playArea.getCardAt(center.move(BL));
        ViewPlaceableCard BRCard = playArea.getCardAt(center.move(BR));

        ViewPlaceableCard botCard = playArea.getCardAt(center.move(BL, BR));
        ViewPlaceableCard botLeftCard = playArea.getCardAt(center.move(BL, BL));
        ViewPlaceableCard botRightCard = playArea.getCardAt(center.move(BR, BR));

        String TLSpaces = printCard.getSpaces(PrintCard.cornerStringAsSpacesLength + PrintCard.cornerRowSpaceCount);
        String inBetweenSpaces = printCard.getSpaces(PrintCard.cornerRowSpaceCount);
    //endregion

    //region Transform Card -> String[5]
        //transform all cards in their String[5] representation, filling corners as needed.
        //each positions has different corner filling requirements
        fillCorners.clear();
        fillCorners.add(TL);
        fillCorners.add(TR);
        if(TLCard == null) fillCorners.add(BL);
        if(TRCard == null) fillCorners.add(BR);
        String[] topCardRows = getCardAsStringRows(topCard, fillCorners);

        fillCorners.clear();
        fillCorners.add(TL);
        fillCorners.add(TR);
        fillCorners.add(BL);
        if(TLCard == null) fillCorners.add(BR);
        String[] topLeftCardRows = getCardAsStringRows(topLeftCard, fillCorners);

        fillCorners.clear();
        fillCorners.add(TL);
        fillCorners.add(TR);
        if(TRCard == null) fillCorners.add(BL);
        fillCorners.add(BR);
        String[] topRightCardRows = getCardAsStringRows(topRightCard, fillCorners);


        fillCorners.clear();
        if(topLeftCard == null && TLCard!=null) fillCorners.add(TL);
        if(leftCard == null && TLCard!=null) fillCorners.add(BL);
        if(midCard == null && TLCard!=null) fillCorners.add(BR);
        if(topCard == null && TLCard!=null) fillCorners.add(TR);
        String[] TLCardRows = getCardAsStringRows(TLCard, fillCorners);


        fillCorners.clear();
        if(topCard == null && TRCard!=null) fillCorners.add(TL);
        if(midCard == null && TRCard!=null) fillCorners.add(BL);
        if(rightCard == null && TRCard!=null) fillCorners.add(BR);
        if(topRightCard == null && TRCard!=null) fillCorners.add(TR);
        String[] TRCardRows = getCardAsStringRows(TRCard, fillCorners);

        fillCorners.clear();
        fillCorners.add(TL);
        fillCorners.add(BL);
        if(BLCard == null) fillCorners.add(BR);
        if(TLCard == null) fillCorners.add(TR);
        String[] leftCardRows = getCardAsStringRows(leftCard, fillCorners);

        fillCorners.clear();
        if(TLCard == null) fillCorners.add(TL);
        if(BLCard == null) fillCorners.add(BL);
        if(BRCard == null) fillCorners.add(BR);
        if(TRCard == null) fillCorners.add(TR);
        String[] midCardRows = getCardAsStringRows(midCard, fillCorners);

        fillCorners.clear();
        if(TRCard == null) fillCorners.add(TL);
        if(BRCard == null) fillCorners.add(BL);
        fillCorners.add(BR);
        fillCorners.add(TR);
        String[] rightCardRows = getCardAsStringRows(rightCard, fillCorners);

        fillCorners.clear();
        if(leftCard == null && BLCard!=null) fillCorners.add(TL);
        if(botLeftCard == null && BLCard!=null) fillCorners.add(BL);
        if(botCard == null && BLCard!=null) fillCorners.add(BR);
        if(midCard == null && BLCard!=null) fillCorners.add(TR);
        String[] BLCardRows = getCardAsStringRows(BLCard, fillCorners);

        fillCorners.clear();
        if(midCard == null && BRCard!=null) fillCorners.add(TL);
        if(botCard == null && BRCard!=null) fillCorners.add(BL);
        if(botRightCard == null && BRCard!=null) fillCorners.add(BR);
        if(rightCard == null && BRCard!=null) fillCorners.add(TR);
        String[] BRCardRows = getCardAsStringRows(BRCard,fillCorners);

        fillCorners.clear();
        if(BLCard == null) fillCorners.add(TL);
        fillCorners.add(BL);
        fillCorners.add(BR);
        if(BRCard == null) fillCorners.add(TR);
        String[] botCardRows = getCardAsStringRows(botCard, fillCorners);

        fillCorners.clear();
        fillCorners.add(TL);
        fillCorners.add(BL);
        fillCorners.add(BR);
        if(BLCard == null) fillCorners.add(TR);
        String[] botLeftCardRows = getCardAsStringRows(botLeftCard, fillCorners);

        fillCorners.clear();
        if(BRCard == null) fillCorners.add(TL);
        fillCorners.add(BL);
        fillCorners.add(BR);
        fillCorners.add(TR);
        String[] botRightCardRows = getCardAsStringRows(botRightCard, fillCorners);
    //endregion

    //region PlayArea print
        print(topLeftCardRows[0]);  print(inBetweenSpaces); print(topCardRows[0]);  print(inBetweenSpaces); print(topRightCardRows[0]); endl();
        print(topLeftCardRows[1]);  print(inBetweenSpaces); print(topCardRows[1]);  print(inBetweenSpaces); print(topRightCardRows[1]); endl();
        print(topLeftCardRows[2]);  print(inBetweenSpaces); print(topCardRows[2]);  print(inBetweenSpaces); print(topRightCardRows[2]); endl();
        print(topLeftCardRows[3]);  print(inBetweenSpaces); print(topCardRows[3]);  print(inBetweenSpaces); print(topRightCardRows[3]); endl();

        print(topLeftCardRows[4]);  print(TLCardRows[0]);   print(topCardRows[4]);  print(TRCardRows[0]);   print(topRightCardRows[4]); endl();
        print(TLSpaces);            print(TLCardRows[1]);   print(inBetweenSpaces); print(TRCardRows[1]);   endl();
        print(TLSpaces);            print(TLCardRows[2]);   print(inBetweenSpaces); print(TRCardRows[2]);   endl();
        print(TLSpaces);            print(TLCardRows[3]);   print(inBetweenSpaces); print(TRCardRows[3]);   endl();

        print(leftCardRows[0]);     print(TLCardRows[4]);   print(midCardRows[0]);  print(TRCardRows[4]);   print(rightCardRows[0]); endl();
        print(leftCardRows[1]);     print(inBetweenSpaces); print(midCardRows[1]);  print(inBetweenSpaces); print(rightCardRows[1]); endl();
        print(leftCardRows[2]);     print(inBetweenSpaces); print(midCardRows[2]);  print(inBetweenSpaces); print(rightCardRows[2]); endl();
        print(leftCardRows[3]);     print(inBetweenSpaces); print(midCardRows[3]);  print(inBetweenSpaces); print(rightCardRows[3]); endl();

        print(leftCardRows[4]);     print(BLCardRows[0]);   print(midCardRows[4]);  print(BRCardRows[0]);   print(rightCardRows[4]); endl();
        print(TLSpaces);            print(BLCardRows[1]);   print(inBetweenSpaces); print(BRCardRows[1]);   endl();
        print(TLSpaces);            print(BLCardRows[2]);   print(inBetweenSpaces); print(BRCardRows[2]);   endl();
        print(TLSpaces);            print(BLCardRows[3]);   print(inBetweenSpaces); print(BRCardRows[3]);   endl();
        print(botLeftCardRows[0]);  print(BLCardRows[4]);   print(botCardRows[0]);  print(BRCardRows[4]);   print(botRightCardRows[0]); endl();

        print(botLeftCardRows[1]);  print(inBetweenSpaces); print(botCardRows[1]);  print(inBetweenSpaces); print(botRightCardRows[1]); endl();
        print(botLeftCardRows[2]);  print(inBetweenSpaces); print(botCardRows[2]);  print(inBetweenSpaces); print(botRightCardRows[2]); endl();
        print(botLeftCardRows[3]);  print(inBetweenSpaces); print(botCardRows[3]);  print(inBetweenSpaces); print(botRightCardRows[3]); endl();
        print(botLeftCardRows[4]);  print(inBetweenSpaces); print(botCardRows[4]);  print(inBetweenSpaces); print(botRightCardRows[4]); endl();
    //endregion

    //region Visible Resources print
        print("\nVisible Resources: ");
        boolean onePrinted = false;
        for(GameResource res : GameResource.values()){
            Integer amount = playArea.getVisibleResources().get(res);
            if(amount != null) {
                if(onePrinted) print(" , ");
                print(ConsoleBackgroundColors.getColorFromEnum(res) + " " + amount + "x " + res + " " + ConsoleBackgroundColors.RESET);
                onePrinted = true;
            }
        } endl();
    //endregion

    }
}
