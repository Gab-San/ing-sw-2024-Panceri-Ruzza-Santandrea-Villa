package it.polimi.ingsw.view;

import it.polimi.ingsw.view.model.cards.*;
import static it.polimi.ingsw.GameResource.*;
import static it.polimi.ingsw.CornerDirection.*;

import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.view.tui.PrintCard;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.LinkedList;
import java.util.List;

public class PrintCardTest {
    private static ViewPlayCard resourceCard_red_0point;
    private static ViewPlayCard resourceCard_green_1point;
    private static ViewPlayCard goldCard_blue_2pointPerQuill;
    private static ViewPlayCard goldCard_purple_2pointPerCorner;
    private static ViewStartCard startCard_3centralResources;
    private static ViewStartCard startCard_1centralResource;
    private static PrintCard printCard;
    
    @BeforeAll
    public static void createCards(){
        printCard = new PrintCard();

        List<ViewCorner> cornerList = new LinkedList<>();
        cornerList.add( new ViewCorner(MUSHROOM, QUILL, TL) );
        cornerList.add( new ViewCorner(LEAF, POTION, TR) );
        cornerList.add( new ViewCorner(WOLF, SCROLL, BL) );
        cornerList.add( new ViewCorner(FILLED, BUTTERFLY, BR) );

        resourceCard_red_0point = new ViewResourceCard("R_red_0point", "", "",
                cornerList, 0, MUSHROOM);
        resourceCard_green_1point = new ViewResourceCard("R_green_1point", "", "",
                cornerList, 1, LEAF);

        List<GameResource> placementCost = new LinkedList<>();
        placementCost.add(MUSHROOM);
        placementCost.add(LEAF);
        placementCost.add(MUSHROOM);
        placementCost.add(WOLF);
        placementCost.add(MUSHROOM);
        goldCard_blue_2pointPerQuill = new ViewGoldCard("G_goldCard_blue_2pointPerQuill", "", "",
                cornerList, 2, WOLF, placementCost, "Q");

        goldCard_purple_2pointPerCorner = new ViewGoldCard("G_goldCard_purple_2pointPerCorner", "", "",
                cornerList, 2, WOLF, placementCost, "Corner");

        List<GameResource> centralFrontResources = new LinkedList<>();
        centralFrontResources.add(MUSHROOM);
        centralFrontResources.add(WOLF);
        centralFrontResources.add(LEAF);
        startCard_3centralResources = new ViewStartCard("S_startCard_3centralResources", "", "",
                cornerList, centralFrontResources);

        centralFrontResources = new LinkedList<>();
        centralFrontResources.add(BUTTERFLY);
        startCard_1centralResource = new ViewStartCard("S_startCard_1centralResource", "", "",
                cornerList, centralFrontResources);
    }

    private void prepareCard(ViewPlaceableCard card, boolean placeOnFront){
        String side = placeOnFront ? "front" : "back";
        System.out.println("Printing " + card.getCardID() + " on " + side);
        if (placeOnFront)
            card.turnFaceUp();
        else card.turnFaceDown();
    }
    private void printCard(ViewPlayCard card, boolean placeOnFront){
        prepareCard(card, placeOnFront);
        printCard.printCard(card);
    }
    private void printCard(ViewStartCard card, boolean placeOnFront){
        prepareCard(card, placeOnFront);
        printCard.printCard(card);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void resourceCardsPrintTest(boolean placeOnFront){
        printCard(resourceCard_red_0point, placeOnFront);
        printCard(resourceCard_green_1point, placeOnFront);
    }
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void goldCardsPrintTest(boolean placeOnFront){
        printCard(goldCard_blue_2pointPerQuill, placeOnFront);
        printCard(goldCard_purple_2pointPerCorner, placeOnFront);
    }
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void startCardsPrintTest(boolean placeOnFront){
        printCard(startCard_3centralResources, placeOnFront);
        printCard(startCard_1centralResource, placeOnFront);
    }
}
