package it.polimi.ingsw.view.tui.scenes;

import it.polimi.ingsw.stub.StubView;
import it.polimi.ingsw.view.model.ViewBoard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static it.polimi.ingsw.view.ViewBoardGenerator.getRandomAvailableColor;

public class PrintEndgameUITest {
    private PrintEndgameUI endgameUI;
    private ViewBoard board;
    private static final String name1 = "Ale";
    private static final String name2 = "Player2";
    private static final String name3 = "Gamba";
    private static final String name4 = "Gianni Morandi è{°°86°°}à";

    @BeforeEach
    void setUp(){
        board = new ViewBoard(new StubView());
        board.addLocalPlayer(name1);
        endgameUI = new PrintEndgameUI(board, true);
        Random random = new Random();
        board.addOpponent(name2);
        board.addOpponent(name3);
        board.addOpponent(name4);

        board.setScore(name1, random.nextInt(1300));
        board.setScore(name2, random.nextInt(1300));
        board.setScore(name3, random.nextInt(1300));
        board.setScore(name4, random.nextInt(1300));

        board.getAllPlayerHands().forEach(h -> h.setColor(getRandomAvailableColor(board)));
    }

    @AfterEach
    void printEndgameUI(){
        endgameUI.print();
    }

    @Test
    void testPrintUI(){}

    @Test
    void tied2Winners(){
        int maxScore = board.getAllPlayerHands().stream()
                .mapToInt(h -> board.getScore(h.getNickname()))
                .max().orElse(0);

        board.setScore(name2, maxScore+5);
        board.setScore(name3, maxScore+5);
    }

    @Test
    void allTiedWinners(){
        board.setScore(name1, 50);
        board.setScore(name2, 50);
        board.setScore(name3, 50);
        board.setScore(name4, 50);
    }

    @Test
    void allTiedWinByDefault(){
        winByDefault();
        allTiedWinners();
    }

    @Test
    void winByDefault(){
        endgameUI = new PrintEndgameUI(board, false);
    }
}
