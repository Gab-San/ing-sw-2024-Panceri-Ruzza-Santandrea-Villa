package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.view.model.*;
import it.polimi.ingsw.view.model.cards.*;
import it.polimi.ingsw.view.tui.PrintPlayArea;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import static it.polimi.ingsw.GameResource.*;
import static it.polimi.ingsw.CornerDirection.*;
import static it.polimi.ingsw.view.ViewCardGenerator.*;

public class PrintPlayAreaTest {
    PrintPlayArea printPlayArea;
    ViewPlayArea playArea;
    Random random;

    @BeforeEach
    void setUp(){
        random = new Random();
        playArea = new ViewPlayArea();
        printPlayArea = new PrintPlayArea(playArea);

        ViewStartCard startCard = getRandomStartingCard();
        playArea.placeStarting(startCard);
    }
    void printOnCenter(Point center){
        System.out.println("Printing PlayArea centered on: (" + center.row() + ", " + center.col() + ")");
        printPlayArea.printPlayArea(center);
    }
    void printPlayArea(Point ...centers){
        for(Point c : centers){
            printOnCenter(c);
            System.out.println("\n\n"); //spacing
        }
    }
    void standardPrintPlayArea(){
        Point zero = new Point(0,0);
        Point top = zero.move(TL, TR);
        Point left = zero.move(TL, BL);
        Point right = zero.move(TR, BR);
        Point bottom = zero.move(BL, BR);
        printPlayArea(zero,top,left,right,bottom);
    }

    @Test
    void testPrintPlayArea_onlyResourceCards(){
        Queue<ViewPlaceableCard> cards = new LinkedList<>(getRandomResourceCards(20, false));

        for(ViewPlaceableCard card : cards){
            int randomCornerIdx = random.nextInt(playArea.getFreeCorners().size());
            ViewCorner corner = playArea.getFreeCorners().get(randomCornerIdx);
            Point position = corner.getCardRef().getPosition().move(corner.getDirection());
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
            Point position = corner.getCardRef().getPosition().move(corner.getDirection());
            playArea.placeCard(position, card);
        }

        standardPrintPlayArea();
    }
    @Test
    void testPrintPlayArea_mixedCards(){
        Queue<ViewPlaceableCard> cards = new LinkedList<>(getRandomCards(40, false));

        for(ViewPlaceableCard card : cards){
            int randomCornerIdx = random.nextInt(playArea.getFreeCorners().size());
            ViewCorner corner = playArea.getFreeCorners().get(randomCornerIdx);
            Point position = corner.getCardRef().getPosition().move(corner.getDirection());
            playArea.placeCard(position, card);
        }

        standardPrintPlayArea();
    }

    @ParameterizedTest
    @ValueSource(booleans = {true,false})
    void testLeftSideEmpty(boolean placeEmptyOnLeftSide){
        Queue<ViewPlaceableCard> cards = new LinkedList<>(getRandomCards(10, false));

        for(ViewPlaceableCard card : cards){
            List<ViewCorner> rightCorners = playArea.getFreeCorners().stream()
                    .filter(c -> c.getDirection() == TR || c.getDirection()==BR)
                    .toList();
            int randomCornerIdx = random.nextInt(rightCorners.size());
            ViewCorner corner = rightCorners.get(randomCornerIdx);
            Point position = corner.getCardRef().getPosition().move(corner.getDirection());
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
                Point position = corner.getCardRef().getPosition().move(corner.getDirection());
                playArea.placeCard(position, card);
            }
        }

        standardPrintPlayArea();
    }
}
