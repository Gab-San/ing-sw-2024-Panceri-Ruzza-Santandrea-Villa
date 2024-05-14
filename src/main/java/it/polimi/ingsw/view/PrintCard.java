package it.polimi.ingsw.view;

import it.polimi.ingsw.view.model.cards.*;
import it.polimi.ingsw.view.model.enums.GameResourceView;

import static it.polimi.ingsw.CornerDirection.*;

public class PrintCard {
    private static final String WHITE = "\u001B[47m"; // Colore bianco per i bordi
    private static final String RESET = "\u001B[0m";  // Resetta il colore
    private static final String RED = "\u001B[41m";
    private static final String PURPLE = "\u001B[45m";
    private static final String GREEN = "\u001B[42m";
    private static final String BLUE = "\033[44m";
    private static final String YELLOW = "\033[43m";
    private static final int cornerRowSpaceCount = 16;  // Exact spacing between Corners
    private static final int middleRowSideSpaceCount = 10;  // Spacing of the middle row sides (excluding the resource character in the center)
    private String colorCode;

    private void setColorCode(GameResourceView color){
        if(color == null) colorCode = YELLOW; //starting + objective have null card color
        else switch (color) {
            case M: //mushroom
                colorCode = RED;
                break;
            case B: //butterfly
                colorCode = PURPLE;
                break;
            case L: //leaf
                colorCode = GREEN;
                break;
            case W: //wolf
                colorCode = BLUE;
                break;
            default: //starting + objective, use as default
                colorCode = YELLOW;
                break;
        }
    }
    private String getSpaces(int length){
        return " ".repeat(Math.max(0, length));
    }
    private String getCornerRow(GameResourceView leftResource, GameResourceView rightResource, String centerString){
        int totalSpaces = cornerRowSpaceCount - centerString.length();
        String halfSpace = getSpaces(totalSpaces/2);
        if(totalSpaces%2 > 0)
            centerString += " ";

        return WHITE + " " + leftResource + " " + RESET + colorCode + halfSpace + centerString + halfSpace + RESET + WHITE + " " + rightResource + " " + RESET;
    }
    private String getCenterRow(GameResourceView centralResource){
        String middleChar;
        if(centralResource != null)
            middleChar = centralResource.toString();
        else middleChar = " ";

        int totalLineLength = middleRowSideSpaceCount + middleChar.length();
        if(totalLineLength%2 > 0) middleChar += " ";

        return colorCode + getSpaces(middleRowSideSpaceCount) + middleChar + getSpaces(middleRowSideSpaceCount) + RESET;
    }

    public void printCard(ViewPlayCard playCard) {
        setColorCode(playCard.getCardColour());

        GameResourceView centralResource = playCard.isFaceUp() ? null : playCard.getCardColour();

        String[] card = new String[5];
        card[0] = getCornerRow(playCard.getCornerResource(TL), playCard.getCornerResource(TR), playCard.getPointsOnPlaceAsString());
        card[1] = getCenterRow(null);
        card[2] = getCenterRow(centralResource); // color == back resource
        card[3] = getCenterRow(null);
        card[4] = getCornerRow(playCard.getCornerResource(BL), playCard.getCornerResource(BR), playCard.getPlacementCostAsString());

        for (String line : card) {
            System.out.println(line);
        }
    }
    public void printCard(ViewStartCard startCard) {
        setColorCode(startCard.getCardColour());

        GameResourceView[] centralResources = startCard.isFaceUp() ? startCard.getCentralFrontResourcesAsArray() : new GameResourceView[3];

        String[] card = new String[5];
        card[0] = getCornerRow(startCard.getCornerResource(TL), startCard.getCornerResource(TR), "");
        card[1] = getCenterRow(centralResources[1]);
        card[2] = getCenterRow(centralResources[0]); // the order is inverted to always have a resource drawn in the middle
        card[3] = getCenterRow(centralResources[2]);
        card[4] = getCornerRow(startCard.getCornerResource(BL), startCard.getCornerResource(BR), "");

        for (String line : card) {
            System.out.println(line);
        }
    }

    public static void main(String[] args) {
        PrintCard testPrinter = new PrintCard();
        // Esempio di utilizzo
        //testPrinter.printCard(/* card instance here */);
    }
}
