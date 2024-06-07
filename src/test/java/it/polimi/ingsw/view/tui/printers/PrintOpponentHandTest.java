package it.polimi.ingsw.view.tui.printers;

import it.polimi.ingsw.stub.StubView;
import it.polimi.ingsw.view.ViewCardGenerator;
import it.polimi.ingsw.view.model.ViewOpponentHand;
import it.polimi.ingsw.view.model.cards.*;
import it.polimi.ingsw.view.tui.ConsoleTextColors;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static it.polimi.ingsw.view.ViewBoardGenerator.fillHandRandomly;

public class PrintOpponentHandTest {
    static PrintHand printHand;
    static ViewOpponentHand hand;
    static Random random;

    @BeforeAll
    static void setUp(){
        hand = new ViewOpponentHand("Test_Player", new StubView());
        printHand = new PrintHand(hand);
        random = new Random();
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
    void printOpponentHandTest(int handSize){
        boolean front = random.nextBoolean();
        System.out.println(ConsoleTextColors.RED_TEXT + "(Front = " + front + ") Hand size = " + handSize + ConsoleTextColors.RESET);
        System.out.flush();
        fillHandRandomly(hand);
        printHand.printHand();
        System.out.println("\n"); //2 line spacing
        System.out.flush();
    }
}
