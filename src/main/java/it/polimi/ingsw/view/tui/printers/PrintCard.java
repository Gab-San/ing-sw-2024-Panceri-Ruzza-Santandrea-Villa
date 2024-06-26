package it.polimi.ingsw.view.tui.printers;

import it.polimi.ingsw.view.model.cards.*;
import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.view.tui.ConsoleColorsCombiner;

import java.util.List;

import static it.polimi.ingsw.CornerDirection.*;
import static it.polimi.ingsw.view.tui.ConsoleBackgroundColors.*;
import static it.polimi.ingsw.view.tui.ConsoleTextColors.BLACK_TEXT;

/**
 * This printer contains all methods to transform a ViewCard into its String[5] representation <br>
 * It also contains some methods to manipulate or print String[5] representations as needed. <br>
 */
public class PrintCard {
    /**
     * Exact spacing between corners.
     */
    static final int cornerRowSpaceCount = 16;
    /**
     * Spacing of the middle row sides (excluding the resource character in the center).
     */
    static final int middleRowSideSpaceCount = 10;
    /**
     * Color with which the corners are displayed.
     */
    static final String cornerColor = ConsoleColorsCombiner.combine(BLACK_TEXT, WHITE);
    /**
     * Length of the corner's formatted string.
     */
    static final int cornerStringLength = (cornerColor + " M " + RESET).length();
    /**
     * Length of the corner's string representation.
     */
    static final int cornerStringAsSpacesLength = (" M ").length();
    private String colorCode;

    /**
     * Default constructor.
     */
    public PrintCard(){}

    /**
     * Sets the background color internally for the card to be constructed next. <br>
     * Accessible by other printers to allow for finer manipulation of this class' methods.
     * @param color the background color of the card as a GameResource
     */
    void setColorCode(GameResource color){
        colorCode = getColorFromEnum(color);
    }

    /**
     * Adds the cardID in text format (e.g. R15, G1, O5) to the given row on the left side. <br>
     * The cardID inserted is offset towards the center by cardIDSpacing (local constant)
     * @param cardRow a row of the card that is not a corner row (preferably the middle row)
     * @param cardID the card's ID
     * @return modified row with added ID
     */
    private String insertID(String cardRow, String cardID){
        int cardIDSpacing = 2;
        return cardRow.replace(
                colorCode + getSpaces(cardID.length()+ cardIDSpacing),
                colorCode + getSpaces(cardIDSpacing) + cardID
        );
    }

    /**
     * Returns a string composed of 'length' spaces, empty if length is less than zero.
     * @param length number of spaces to return
     * @return a string composed of 'length' spaces, empty if length &lt;= 0
     */
    public String getSpaces(int length){
        return " ".repeat(Math.max(0, length));
    }

    /**
     * Constructs a corner row (either top or bottom) with the two resources as string initials <br>
     * This should be used to construct rows 0 and 4
     * @param leftResource resource of the left corner of the row to be constructed
     * @param rightResource resource of the right corner of the row to be constructed
     * @param centerString string representation of pointsOnPlace, or what should be printed in the center of this row
     * @return the constructed corner row, already colored and formatted with ANSI characters.
     *          Works for both the top and bottom corner rows.
     */
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

    /**
     * Constructs a center row with the (optional) single resource as string initial in the middle <br>
     * This should be used to construct rows 1,2,3
     * @param centralResource resource to be printed in the center of the row to be constructed
     * @return the constructed center row, already colored and formatted with ANSI characters
     */
    private String getCenterRow(GameResource centralResource){
        String middleChar;
        if(centralResource != null)
            middleChar = centralResource.toString();
        else middleChar = " ";

        int totalLineLength = middleRowSideSpaceCount + middleChar.length();
        if(totalLineLength%2 > 0) middleChar += " ";

        return colorCode + getSpaces(middleRowSideSpaceCount) + middleChar + getSpaces(middleRowSideSpaceCount) + RESET;
    }

    /**
     * @param playCard a ViewPlayCard to represent as String[5]
     * @param hideID true if the card's ID digits should be hidden (only the letter would be shown).
     * @return the PlayCard's String[5] representation. An array of 5 strings top->bottom.<br>
     * Already colored and formatted with ANSI characters.
     */
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
    /**
     * @param startCard a ViewStartCard to represent as String[5]
     * @param hideID true if the card's ID digits should be hidden (only the letter would be shown).
     * @return the StartCard's String[5] representation. An array of 5 strings top->bottom.<br>
     * Already colored and formatted with ANSI characters.
     */
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

    /**
     * Defaults getCardAsStringRows(ViewCard, hideID) to hideID = false
     * @param card any ViewCard to be transformed in String[5] representation
     * @return the Card's String[5] representation. An array of 5 strings top->bottom.<br>
     * Already colored and formatted with ANSI characters.
     */
    public String[] getCardAsStringRows(ViewCard card){
        return getCardAsStringRows(card, false);
    }
    /**
     * This method delegates the creation of the String[5] representation to the specific
     * (private) implementation of getCardAsStringRows for the effective type of the card.
     * @param card any ViewCard to be transformed in String[5] representation.
     *             A null value is interpreted as an "empty" card.
     * @param hideID true if the card's ID digits should be hidden (only the letter would be shown).
     * @return the Card's String[5] representation. An array of 5 strings top->bottom.<br>
     * Already colored and formatted with ANSI characters. <br>
     * A null card (empty) is transformed into 5 rows of spaces with the same display length
     * as non-null cards, to be printed alongside other non-null cards. <br>
     * *Please note* that the corners of a null card are still colored as normal corners and should
     * be processed by cutAllCornersIfEmpty(String[5]) to be replaced by spaces with no coloring.
     */
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

    /**
     * @param pattern the pattern of an ObjectiveCard (format: '*B* B** R*R'). <br>
     *                Each section of 3 characters is considered a row of the pattern, in order top -> bottom <br>
     *                Each row must have 3 characters and be separated from other rows by a space
     * @param rowIdx which of the (0-2) rows of the pattern to print on the card row to return
     * @return the spaced pattern row to add in the objective card String[5] representation
     */
    private String getPatternRow(String pattern, int rowIdx){
        String row = pattern.split(" ")[rowIdx];
        String spacing = getSpaces(2);
        return row.charAt(0) + spacing + row.charAt(1) + spacing + row.charAt(2);
    }
    /**
     * @param objCard a ViewObjectiveCard to represent as String[5]
     * @param hideID true if the card's ID digits should be hidden (only the letter would be shown).
     * @return the ObjectiveCard's String[5] representation. An array of 5 strings top->bottom.<br>
     * Already colored and formatted with ANSI characters.
     */
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

    /**
     * Prints any ViewCard on System.out in its String[5] representation. <br>
     * @param card the ViewCard to print
     */
    public void printCard(ViewCard card) {
        for (String line : getCardAsStringRows(card)) {
            System.out.println(line);
        }
    }
    /**
     * Prints a list of ViewCards on System.out in their String[5] representation. <br>
     * The cards are printed in a row left -> right in the order they are in the List
     * @param cardsAsStringRows the ViewCards to print
     * @param cardSpacing the spacing between printed cards
     */
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

    /**
     * Returns the unchanged cardAsStringRows if it wasn't a null card's String[5] representation. <br>
     * Otherwise, returns a String[5] of just spaces with no coloring.
     * @param cardAsStringRows any ViewCard's String[5] representation, even a null card's
     * @return the unchanged cardAsStringRows if it wasn't a null card's String[5] representation. <br>
     * Otherwise, returns a String[5] of just spaces with no coloring.
     */
    public String[] cutAllCornersIfEmpty(String[] cardAsStringRows){
        String emptyRow = getSpaces(cornerStringAsSpacesLength*2 + cornerRowSpaceCount);
        if(cardAsStringRows[2].equals(emptyRow)){
            cardAsStringRows[0] = emptyRow;
            cardAsStringRows[4] = emptyRow;
        }
        return cardAsStringRows;
    }
}
