package it.polimi.ingsw.view.tui.printers;

import it.polimi.ingsw.view.model.ViewPlayerHand;
import it.polimi.ingsw.view.tui.ConsoleTextColors;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static it.polimi.ingsw.view.ViewBoardGenerator.fillHandRandomly;

public abstract class PrintPlayerHandTest {
    static PrintHand printPlayerHand;
    static ViewPlayerHand hand;

    @BeforeAll
    static void setUp(){
        hand = new ViewPlayerHand("Test_Player");
        printPlayerHand = new PrintHand(hand);
    }

    @ParameterizedTest
    @ValueSource(ints = {0,1,2,3})
    void printPlayerHandTest(int handSize){
        System.out.println(ConsoleTextColors.RED_TEXT + "Hand size = " + handSize + ConsoleTextColors.RESET);
        System.out.flush();
        fillHandRandomly(hand);
        printPlayerHand.printHand();
        System.out.println("\n"); //2 line spacing
        System.out.flush();
    }
}
