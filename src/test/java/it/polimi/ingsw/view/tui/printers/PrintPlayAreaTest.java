package it.polimi.ingsw.view.tui.printers;

import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.stub.StubView;
import it.polimi.ingsw.view.model.*;
import it.polimi.ingsw.view.model.cards.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import static it.polimi.ingsw.CornerDirection.*;
import static it.polimi.ingsw.view.ViewCardGenerator.*;
import static it.polimi.ingsw.view.tui.ConsoleTextColors.RED_TEXT;
import static it.polimi.ingsw.view.tui.ConsoleTextColors.RESET;
import static org.junit.jupiter.api.Assertions.fail;

public class PrintPlayAreaTest {
    PrintPlayArea printPlayArea;
    ViewPlayArea playArea;
    Random random;

    @BeforeEach
    void setUp(){
        random = new Random();
        ViewBoard board = new ViewBoard(new StubView());
        board.addLocalPlayer("Test_Player");

        playArea = board.getPlayerArea("Test_Player");
        printPlayArea = new PrintPlayArea(playArea);

        ViewStartCard startCard = getRandomStartingCard();
        playArea.placeCard(new GamePoint(0,0), startCard);
    }
    void printOnCenter(GamePoint center){
        System.out.println("Printing PlayArea centered on: (" + center.row() + ", " + center.col() + ")");
        printPlayArea.printPlayArea(center);
    }
    void printPlayArea(GamePoint...centers){
        playArea.calculateZLayers();
        for(GamePoint c : centers){
            printOnCenter(c);
            System.out.println("\n\n"); //spacing
        }
    }
    void standardPrintPlayArea(){
        GamePoint zero = new GamePoint(0,0);
        GamePoint top = zero.move(TL, TR);
        GamePoint left = zero.move(TL, BL);
        GamePoint right = zero.move(TR, BR);
        GamePoint bottom = zero.move(BL, BR);
        printPlayArea(zero,top,left,right,bottom);
    }

    @Test
    void testPrintPlayArea_onlyResourceCards(){
        Queue<ViewPlaceableCard> cards = new LinkedList<>(getRandomResourceCards(20, false));

        for(ViewPlaceableCard card : cards){
            int randomCornerIdx = random.nextInt(playArea.getFreeCorners().size());
            ViewCorner corner = playArea.getFreeCorners().get(randomCornerIdx);
            GamePoint position = corner.getCardRef().getPosition().move(corner.getDirection());
            playArea.placeCard(position, card);
        }

        standardPrintPlayArea();
    }
    @Test
    void testPrintPlayArea_onlyGoldCards(){
        Queue<ViewPlaceableCard> cards = new LinkedList<>(getRandomGoldCards(20, false));

        for(ViewPlaceableCard card : cards){
            int randomCornerIdx = random.nextInt(playArea.getFreeCorners().size());
            ViewCorner corner = playArea.getFreeCorners().get(randomCornerIdx);
            GamePoint position = corner.getCardRef().getPosition().move(corner.getDirection());
            playArea.placeCard(position, card);
        }

        standardPrintPlayArea();
    }
    @Test
    void testPrintPlayArea_mixedCards(){
        PlaceOnPlayArea_mixedCards(40);
        standardPrintPlayArea();
    }
    private void PlaceOnPlayArea_mixedCards(int cardNum){
        Queue<ViewPlaceableCard> cards = new LinkedList<>(getRandomCards(cardNum, false));

        for(ViewPlaceableCard card : cards){
            if(playArea.getFreeCorners().isEmpty()){
                System.out.println("DEADLOCK!");
                System.out.println("Stopping at "+ playArea.getCardMatrix().size() +" cards!");
                return;
            }
            int randomCornerIdx = random.nextInt(playArea.getFreeCorners().size());
            ViewCorner corner = playArea.getFreeCorners().get(randomCornerIdx);
            GamePoint position = corner.getCardRef().getPosition().move(corner.getDirection());
            playArea.placeCard(position, card);
        }
    }
    @ParameterizedTest
    @ValueSource(ints={0,1,5,10,20,40,50}) //there are at most 80 cards in the deck
    void zLayerValidationTest(int i){
        try {
            System.out.println("PRINTING " + i);
            PlaceOnPlayArea_mixedCards(i);
            printPlayArea(new GamePoint(0,0));
            playArea.calculateZLayers();
        }catch (IllegalArgumentException e){
            System.out.println("\n\n" + RED_TEXT + e.getMessage() + RESET);
            fail(RED_TEXT + "FAILED AT " + i + RESET);
        }
    }
//    @RepeatedTest(10000)
//    void looped_ZLayer_validation(){
//        int[] tries = new int[]{20, 40, 80, 120};
//        for (int i : tries){
//            setUp();
//            zLayerValidationTest(i);
//        }
//    }

    @ParameterizedTest
    @ValueSource(booleans = {true,false})
    void testLeftSideEmpty(boolean placeEmptyOnLeftSide){
        Queue<ViewPlaceableCard> cards = new LinkedList<>(getRandomCards(10, false));

        for(ViewPlaceableCard card : cards){
            List<ViewCorner> rightCorners;
            synchronized (playArea.getFreeCorners()) {
                rightCorners = playArea.getFreeCorners().stream()
                        .filter(c -> c.getDirection() == TR || c.getDirection() == BR)
                        .toList();
            }
            if(rightCorners.isEmpty()) continue;
            int randomCornerIdx = random.nextInt(rightCorners.size());
            ViewCorner corner = rightCorners.get(randomCornerIdx);
            GamePoint position = corner.getCardRef().getPosition().move(corner.getDirection());
            playArea.placeCard(position, card);
        }

        if(placeEmptyOnLeftSide) {
            cards = new LinkedList<>(getEmptyCards(20, false));
            for (ViewPlaceableCard card : cards) {
                List<ViewCorner> leftCorners = playArea.getFreeCorners().stream()
                        .filter(c -> c.getDirection() == TL || c.getDirection() == BL)
                        .toList();
                int randomCornerIdx = random.nextInt(leftCorners.size());
                ViewCorner corner = leftCorners.get(randomCornerIdx);
                GamePoint position = corner.getCardRef().getPosition().move(corner.getDirection());
                playArea.placeCard(position, card);
            }
        }

        standardPrintPlayArea();
    }
}
