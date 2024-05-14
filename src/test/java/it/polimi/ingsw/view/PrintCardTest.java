package it.polimi.ingsw.view;

import it.polimi.ingsw.view.model.cards.*;
import static it.polimi.ingsw.view.model.enums.GameResourceView.*;
import static it.polimi.ingsw.CornerDirection.*;

import it.polimi.ingsw.view.model.enums.GameResourceView;
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
        cornerList.add( new ViewCorner(M, Q, TL) );
        cornerList.add( new ViewCorner(L, P, TR) );
        cornerList.add( new ViewCorner(W, S, BL) );
        cornerList.add( new ViewCorner(F, B, BR) );

        resourceCard_red_0point = new ViewResourceCard("R_red_0point", "", "",
                cornerList, 0, M);
        resourceCard_green_1point = new ViewResourceCard("R_green_1point", "", "",
                cornerList, 1, L);

        List<GameResourceView> placementCost = new LinkedList<>();
        placementCost.add(M);
        placementCost.add(L);
        placementCost.add(M);
        placementCost.add(W);
        placementCost.add(M);
        goldCard_blue_2pointPerQuill = new ViewGoldCard("G_goldCard_blue_2pointPerQuill", "", "",
                cornerList, 2, W, placementCost, "Q");

        goldCard_purple_2pointPerCorner = new ViewGoldCard("G_goldCard_purple_2pointPerCorner", "", "",
                cornerList, 2, W, placementCost, "Corner");

        List<GameResourceView> centralFrontResources = new LinkedList<>();
        centralFrontResources.add(M);
        centralFrontResources.add(W);
        centralFrontResources.add(L);
        startCard_3centralResources = new ViewStartCard("S_startCard_3centralResources", "", "",
                cornerList, centralFrontResources);

        centralFrontResources = new LinkedList<>();
        centralFrontResources.add(B);
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
