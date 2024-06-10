package it.polimi.ingsw.view.tui.scenes;

import it.polimi.ingsw.stub.StubView;
import it.polimi.ingsw.view.model.ViewBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import java.util.Random;

import static it.polimi.ingsw.view.ViewBoardGenerator.getRandomAvailableColor;

public class PrintEndgameUITest {
    private PrintEndgameUI endgameUI;
    private static final String name1 = "Ale";
    private static final String name2 = "Player2";
    private static final String name3 = "Gamba";
    private static final String name4 = "Gianni Morandi è{°°86°°}à";

    @BeforeEach
    void setUp(){
        ViewBoard board = new ViewBoard(new StubView());
        board.addLocalPlayer(name1);
        endgameUI = new PrintEndgameUI(board, false);
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

    @RepeatedTest(15)
    void printEndgameUI(){
        endgameUI.print();
    }

}
