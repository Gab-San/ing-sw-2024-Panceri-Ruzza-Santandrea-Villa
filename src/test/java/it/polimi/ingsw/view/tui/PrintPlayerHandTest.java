package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.view.ViewCardGenerator;
import it.polimi.ingsw.view.model.ViewPlayerHand;
import it.polimi.ingsw.view.model.cards.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public abstract class PrintPlayerHandTest {
    static PrintHand printPlayerHand;
    static ViewPlayerHand hand;

    @BeforeAll
    static void setUp(){
        hand = new ViewPlayerHand("Test_Player");
        printPlayerHand = new PrintHand(hand);
    }

    static List<ViewPlayCard> getRandomPlayCardList(int num, boolean allFront){
        List<ViewPlaceableCard> cardList = ViewCardGenerator.getRandomCards(num, allFront);
        List<ViewPlayCard> playList = new LinkedList<>();
        for(ViewPlaceableCard card : cardList){
            playList.add((ViewPlayCard) card);
        }
        return playList;
    }
    static List<ViewObjectiveCard> getRandomObjectiveCardList(){
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
        System.out.println(ConsoleTextColors.RED_TEXT + "Hand size = " + handSize + ConsoleTextColors.RESET);
        System.out.flush();
        hand.setStartCard(ViewCardGenerator.getRandomStartingCard());
        hand.setCards(getRandomPlayCardList(handSize, true));
        hand.setSecretObjectiveCards(getRandomObjectiveCardList());
        printPlayerHand.printHand();
        System.out.println("\n"); //2 line spacing
        System.out.flush();
    }
}
