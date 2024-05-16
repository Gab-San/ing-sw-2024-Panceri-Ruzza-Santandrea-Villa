package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.view.model.cards.*;
import it.polimi.ingsw.GameResource;

import java.util.List;

import static it.polimi.ingsw.CornerDirection.*;
import static it.polimi.ingsw.view.tui.ConsoleBackgroundColors.*;

public class PrintCard {
    static final int cornerRowSpaceCount = 16;  // Exact spacing between Corners
    static final int middleRowSideSpaceCount = 10;  // Spacing of the middle row sides (excluding the resource character in the center)
    static final int cornerStringLength = (WHITE + " M " + RESET).length();
    static final int cornerStringAsSpacesLength = (WHITE + " M " + RESET).length() - (WHITE + RESET).length();
    private final int cardIDSpacing = 2;
    private String colorCode;

    void setColorCode(GameResource color){
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
    private String insertID(String cardRow, String cardID){
        return cardRow.replace(
                colorCode + getSpaces(cardID.length()+cardIDSpacing),
                colorCode + getSpaces(cardIDSpacing) + cardID
        );
    }
    String getSpaces(int length){
        return " ".repeat(Math.max(0, length));
    }
    String getCornerRow(GameResource leftResource, GameResource rightResource, String centerString){
        int totalSpaces = cornerRowSpaceCount - centerString.length();
        String halfSpace = getSpaces(totalSpaces/2);
        if(totalSpaces%2 > 0)
            centerString += " ";

        String leftResourceStr = leftResource == null ? " " : leftResource.toString();
        String rightResourceStr = rightResource == null ? " " : rightResource.toString();

        return WHITE + " " + leftResourceStr + " " + RESET + colorCode + halfSpace + centerString + halfSpace + RESET + WHITE + " " + rightResourceStr + " " + RESET;
    }
    String getCenterRow(GameResource centralResource){
        String middleChar;
        if(centralResource != null)
            middleChar = centralResource.toString();
        else middleChar = " ";

        int totalLineLength = middleRowSideSpaceCount + middleChar.length();
        if(totalLineLength%2 > 0) middleChar += " ";

        return colorCode + getSpaces(middleRowSideSpaceCount) + middleChar + getSpaces(middleRowSideSpaceCount) + RESET;
    }

    String[] getCardAsStringRows(ViewPlayCard playCard){
        setColorCode(playCard.getCardColour());

        GameResource centralResource = playCard.isFaceUp() ? null : playCard.getCardColour();

        String[] card = new String[5];
        card[0] = getCornerRow(playCard.getCornerResource(TL), playCard.getCornerResource(TR), playCard.getPointsOnPlaceAsString());
        card[1] = getCenterRow(null);
        card[2] = getCenterRow(centralResource); // color == back resource
        card[3] = getCenterRow(null);
        card[4] = getCornerRow(playCard.getCornerResource(BL), playCard.getCornerResource(BR), playCard.getPlacementCostAsString());

        card[2] = insertID(card[2], playCard.getCardID());
        return card;
    }
    String[] getCardAsStringRows(ViewStartCard startCard) {
        setColorCode(startCard.getCardColour());

        GameResource[] centralResources = startCard.isFaceUp() ? startCard.getCentralFrontResourcesAsArray() : new GameResource[3];

        String[] card = new String[5];
        card[0] = getCornerRow(startCard.getCornerResource(TL), startCard.getCornerResource(TR), "");
        card[1] = getCenterRow(centralResources[1]);
        card[2] = getCenterRow(centralResources[0]); // the order is inverted to always have a resource drawn in the middle
        card[3] = getCenterRow(centralResources[2]);
        card[4] = getCornerRow(startCard.getCornerResource(BL), startCard.getCornerResource(BR), "");

        card[2] = insertID(card[2], startCard.getCardID());
        return card;
    }
    String[] getCardAsStringRows(ViewPlaceableCard card){
        if(card == null){
            String[] nullCardAsSpaces = new String[5];
            for (int i = 1; i < nullCardAsSpaces.length-1; i++) {
                nullCardAsSpaces[i] = getSpaces(cornerStringAsSpacesLength*2 + cornerRowSpaceCount);
            }
            String cornerSubstitute = WHITE + " E " + RESET;
            nullCardAsSpaces[0] = cornerSubstitute + getSpaces(cornerRowSpaceCount) + cornerSubstitute;
            nullCardAsSpaces[4] = nullCardAsSpaces[0];
            return nullCardAsSpaces;
        }

        // FIXME: [Ale] non mi piace usare instanceof, ma non mi viene un'altra soluzione.
        //          Il metodo getCardAsStringRows deve sapere se è play o starting card.
        if(card instanceof ViewPlayCard playCard)
            return getCardAsStringRows(playCard);
        else return getCardAsStringRows((ViewStartCard) card);
    }

    private String getPatternRow(String pattern, int rowIdx){
        String row = pattern.split(" ")[rowIdx];
        String spacing = getSpaces(2);
        return row.charAt(0) + spacing + row.charAt(1) + spacing + row.charAt(2);
    }
    public String[] getCardAsStringRows(ViewObjectiveCard objCard){
        setColorCode(objCard.getCardColour());

        String[] card = new String[5];
        String emptyRow = getSpaces(cornerStringAsSpacesLength*2 + cornerRowSpaceCount);
        card[0] = emptyRow;
        card[1] = objCard.isPatternType() ? getPatternRow(objCard.getObjectiveStrategyValue(), 0) : emptyRow;
        card[2] = objCard.isPatternType() ? getPatternRow(objCard.getObjectiveStrategyValue(), 1) : objCard.getObjectiveStrategyValue();
        card[3] = objCard.isPatternType() ? getPatternRow(objCard.getObjectiveStrategyValue(), 2) : emptyRow;
        card[4] = card[0];

        for (int i = 1; i <= 3; i++) {
            int missingSpaces = card[0].length() - card[i].length();
            if(missingSpaces%2 > 0) card[i] += " ";
            String padding = getSpaces(missingSpaces/2);
            card[i] = padding + card[i] + padding;
        }
        for (int i = 0; i < card.length; i++) {
            card[i] = colorCode + card[i] + RESET;
        }

        card[2] = insertID(card[2], objCard.getCardID());
        return card;
    }

    public void printCard(ViewPlaceableCard card) {
        for (String line : getCardAsStringRows(card)) {
            System.out.println(line);
        }
    }
    public void printCard(ViewObjectiveCard card) {
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
}
