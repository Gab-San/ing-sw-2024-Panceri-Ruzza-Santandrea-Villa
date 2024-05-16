package it.polimi.ingsw.view;

import it.polimi.ingsw.view.model.ViewPlayerHand;
import it.polimi.ingsw.view.model.cards.*;
import it.polimi.ingsw.view.tui.PrintPlayerUI;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class PrintPlayerUITest {
    static PrintPlayerUI printPlayerUI;
    static ViewPlayerHand hand;

    @BeforeAll
    static void setUp(){
        printPlayerUI = new PrintPlayerUI();
        hand = new ViewPlayerHand("Test_Player");
    }

    private static List<ViewPlayCard> getRandomPlayCardList(int num, boolean allFront){
        List<ViewPlaceableCard> cardList = ViewCardGenerator.getRandomCards(num, allFront);
        List<ViewPlayCard> playList = new LinkedList<>();
        for(ViewPlaceableCard card : cardList){
            playList.add((ViewPlayCard) card);
        }
        return playList;
    }
    private static List<ViewObjectiveCard> getObjectiveCardList(){
        List<ViewObjectiveCard> objList = new LinkedList<>();
        Random random = new Random();
        if(random.nextBoolean())
            objList.add(ViewCardGenerator.getRandomObjectiveCard(random.nextBoolean()));
        if(random.nextBoolean())
            objList.add(ViewCardGenerator.getRandomObjectiveCard(random.nextBoolean()));
        return objList;
    }

    @ParameterizedTest
    @ValueSource(ints = {0,1,2,3})
    void printPlayerHandTest(int handSize){
        System.err.println("Hand size = " + handSize);
        System.err.flush();
        hand.setStartCard((ViewStartCard) ViewCardGenerator.getRandomStartingCard());
        hand.setCards(getRandomPlayCardList(handSize, true));
        hand.setSecretObjectiveCards(getObjectiveCardList());
        printPlayerUI.printHand(hand);
        System.out.flush();
        System.err.println("\n"); //2 line spacing
        System.err.flush();
    }
}
