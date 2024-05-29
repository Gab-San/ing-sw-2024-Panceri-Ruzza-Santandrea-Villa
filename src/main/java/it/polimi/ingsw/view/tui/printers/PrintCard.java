package it.polimi.ingsw.view.tui.printers;

import it.polimi.ingsw.view.model.cards.*;
import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.view.tui.ConsoleColorsCombiner;

import java.util.List;

import static it.polimi.ingsw.CornerDirection.*;
import static it.polimi.ingsw.view.tui.ConsoleBackgroundColors.*;
import static it.polimi.ingsw.view.tui.ConsoleTextColors.BLACK_TEXT;

public class PrintCard {
    static final int cornerRowSpaceCount = 16;  // Exact spacing between Corners
    static final int middleRowSideSpaceCount = 10;  // Spacing of the middle row sides (excluding the resource character in the center)
    static final String cornerColor = ConsoleColorsCombiner.combine(BLACK_TEXT, WHITE);
    static final int cornerStringLength = (cornerColor + " M " + RESET).length();
    static final int cornerStringAsSpacesLength = (" M ").length();
    private final int cardIDSpacing = 2;
    private String colorCode;

    void setColorCode(GameResource color){
        colorCode = getColorFromEnum(color);
    }
    private String insertID(String cardRow, String cardID){
        return cardRow.replace(
                colorCode + getSpaces(cardID.length()+cardIDSpacing),
                colorCode + getSpaces(cardIDSpacing) + cardID
        );
    }
    public String getSpaces(int length){
        return " ".repeat(Math.max(0, length));
    }
    private String getCornerRow(GameResource leftResource, GameResource rightResource, String centerString){
        int totalSpaces = cornerRowSpaceCount - centerString.length();
        String halfSpace = getSpaces(totalSpaces/2);
        if(totalSpaces%2 > 0)
            centerString += " ";

        String leftResourceStr = leftResource == null ? " " : leftResource.toString();
        String rightResourceStr = rightResource == null ? " " : rightResource.toString();

        return cornerColor + " " + leftResourceStr + " " + RESET +
                colorCode + halfSpace + centerString + halfSpace + RESET +
                cornerColor + " " + rightResourceStr + " " + RESET;
    }
    private String getCenterRow(GameResource centralResource){
        String middleChar;
        if(centralResource != null)
            middleChar = centralResource.toString();
        else middleChar = " ";

        int totalLineLength = middleRowSideSpaceCount + middleChar.length();
        if(totalLineLength%2 > 0) middleChar += " ";

        return colorCode + getSpaces(middleRowSideSpaceCount) + middleChar + getSpaces(middleRowSideSpaceCount) + RESET;
    }

    private String[] getCardAsStringRows(ViewPlayCard playCard, boolean hideID){
        setColorCode(playCard.getCardColour());

        GameResource centralResource = playCard.isFaceUp() ? null : playCard.getCardColour();

        String[] card = new String[5];
        card[0] = getCornerRow(playCard.getCornerResource(TL), playCard.getCornerResource(TR), playCard.getPointsOnPlaceAsString());
        card[1] = getCenterRow(null);
        card[2] = getCenterRow(centralResource); // color == back resource
        card[3] = getCenterRow(null);
        card[4] = getCornerRow(playCard.getCornerResource(BL), playCard.getCornerResource(BR), playCard.getPlacementCostAsString());

        String ID = playCard.getCardID();
        card[2] = insertID(card[2], playCard.isFaceUp() || !hideID ? ID : ID.charAt(0)+" ");
        return card;
    }
    private String[] getCardAsStringRows(ViewStartCard startCard, boolean hideID) {
        setColorCode(startCard.getCardColour());

        GameResource[] centralResources = startCard.isFaceUp() ? startCard.getCentralFrontResourcesAsArray() : new GameResource[3];

        String[] card = new String[5];
        card[0] = getCornerRow(startCard.getCornerResource(TL), startCard.getCornerResource(TR), "");
        card[1] = getCenterRow(centralResources[1]);
        card[2] = getCenterRow(centralResources[0]); // the order is inverted to always have a resource drawn in the middle
        card[3] = getCenterRow(centralResources[2]);
        card[4] = getCornerRow(startCard.getCornerResource(BL), startCard.getCornerResource(BR), "");

        card[2] = insertID(card[2], startCard.isFaceUp() || !hideID ? startCard.getCardID() : "S ");
        return card;
    }
    public String[] getCardAsStringRows(ViewCard card){
        return getCardAsStringRows(card, false);
    }
    public String[] getCardAsStringRows(ViewCard card, boolean hideID){
        if(card == null){
            String[] nullCardAsSpaces = new String[5];
            for (int i = 1; i < nullCardAsSpaces.length-1; i++) {
                nullCardAsSpaces[i] = getSpaces(cornerStringAsSpacesLength*2 + cornerRowSpaceCount);
            }
            String cornerSubstitute = cornerColor + "   " + RESET;
            nullCardAsSpaces[0] = cornerSubstitute + getSpaces(cornerRowSpaceCount) + cornerSubstitute;
            nullCardAsSpaces[4] = nullCardAsSpaces[0];
            return nullCardAsSpaces;
        }

        if(card instanceof ViewPlayCard playCard)
            return getCardAsStringRows(playCard, hideID);
        else if(card instanceof ViewStartCard startCard)
            return getCardAsStringRows(startCard, hideID);
        else return getCardAsStringRows((ViewObjectiveCard) card, hideID);
        // ViewCard is abstract, the card must be of 1 of the 3 types, no need to explicitly check the last instanceof ViewObjectiveCard
    }

    private String getPatternRow(String pattern, int rowIdx){
        String row = pattern.split(" ")[rowIdx];
        String spacing = getSpaces(2);
        return row.charAt(0) + spacing + row.charAt(1) + spacing + row.charAt(2);
    }
    private String[] getCardAsStringRows(ViewObjectiveCard objCard, boolean hideID){
        setColorCode(objCard.getCardColour());

        String[] card = new String[5];
        String emptyRow = getSpaces(cornerStringAsSpacesLength*2 + cornerRowSpaceCount);
        int pointPerSolveLength = Integer.toString(objCard.getPointsPerSolve()).length();
        if(objCard.isFaceUp())
                                            //-1 is needed as middleRowSideSpaceCount already considers a character to print in the middle
            card[0] = getSpaces(middleRowSideSpaceCount - (pointPerSolveLength-1)/2) + objCard.getPointsPerSolve() + getSpaces(middleRowSideSpaceCount + pointPerSolveLength%2);
        else card[0] = emptyRow;
        card[1] = objCard.isPatternType() && objCard.isFaceUp() ? getPatternRow(objCard.getObjectiveStrategyAsString(), 0) : emptyRow;
        if(objCard.isFaceUp())
            card[2] = objCard.isPatternType() ? getPatternRow(objCard.getObjectiveStrategyAsString(), 1) : objCard.getObjectiveStrategyAsString();
        else card[2] = emptyRow;
        card[3] = objCard.isPatternType() && objCard.isFaceUp() ? getPatternRow(objCard.getObjectiveStrategyAsString(), 2) : emptyRow;
        card[4] = emptyRow;

        for (int i = 1; i <= 3; i++) {
            int missingSpaces = emptyRow.length() - card[i].length();
            if(missingSpaces < 0) continue;
            if(missingSpaces%2 > 0) card[i] += " ";
            String padding = getSpaces(missingSpaces/2);
            card[i] = padding + card[i] + padding;
        }
        for (int i = 0; i < card.length; i++) {
            card[i] = colorCode + card[i] + RESET;
        }

        card[2] = insertID(card[2], objCard.isFaceUp() || !hideID ? objCard.getCardID() : "O ");
        return card;
    }

    public void printCard(ViewCard card) {
        for (String line : getCardAsStringRows(card)) {
            System.out.println(line);
        }
    }
    public void printCardsSideBySide(List<String[]> cardsAsStringRows, int cardSpacing){
        if(cardsAsStringRows.isEmpty()) return;

        String spacing = getSpaces(cardSpacing);
        for (int i = 0; i < cardsAsStringRows.get(0).length; i++) {
            for (String[] cardAsRows : cardsAsStringRows){
                System.out.print(cardAsRows[i] + spacing);
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }

    public String[] cutAllCornersIfEmpty(String[] cardAsStringRows){
        String emptyRow = getSpaces(cornerStringAsSpacesLength*2 + cornerRowSpaceCount);
        if(cardAsStringRows[2].equals(emptyRow)){
            cardAsStringRows[0] = emptyRow;
            cardAsStringRows[4] = emptyRow;
        }
        return cardAsStringRows;
    }
}
