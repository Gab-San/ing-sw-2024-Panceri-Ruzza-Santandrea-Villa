package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.view.model.cards.*;
import it.polimi.ingsw.GameResource;

import static it.polimi.ingsw.CornerDirection.*;
import static it.polimi.ingsw.view.tui.ConsoleBackgroundColors.*;

public class PrintCard {
    static final int cornerRowSpaceCount = 16;  // Exact spacing between Corners
    static final int middleRowSideSpaceCount = 10;  // Spacing of the middle row sides (excluding the resource character in the center)
    private String colorCode;

    private void setColorCode(GameResource color){
        if(color == null) colorCode = YELLOW; //starting + objective have null card color
        else switch (color) {
            case MUSHROOM: //mushroom
                colorCode = RED;
                break;
            case BUTTERFLY: //butterfly
                colorCode = PURPLE;
                break;
            case LEAF: //leaf
                colorCode = GREEN;
                break;
            case WOLF: //wolf
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
    private String getCornerRow(GameResource leftResource, GameResource rightResource, String centerString){
        int totalSpaces = cornerRowSpaceCount - centerString.length();
        String halfSpace = getSpaces(totalSpaces/2);
        if(totalSpaces%2 > 0)
            centerString += " ";

        return WHITE + " " + leftResource + " " + RESET + colorCode + halfSpace + centerString + halfSpace + RESET + WHITE + " " + rightResource + " " + RESET;
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

    public void printCard(ViewPlayCard playCard) {
        setColorCode(playCard.getCardColour());

        GameResource centralResource = playCard.isFaceUp() ? null : playCard.getCardColour();

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

        GameResource[] centralResources = startCard.isFaceUp() ? startCard.getCentralFrontResourcesAsArray() : new GameResource[3];

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
