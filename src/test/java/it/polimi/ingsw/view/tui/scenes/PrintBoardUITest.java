package it.polimi.ingsw.view.tui.scenes;

import static it.polimi.ingsw.view.ViewBoardGenerator.*;
import static it.polimi.ingsw.view.ViewCardGenerator.*;

import it.polimi.ingsw.view.model.ViewBoard;
import it.polimi.ingsw.view.model.ViewHand;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Random;

public class PrintBoardUITest {
    PrintBoardUI printBoardUI;
    ViewBoard board;
    Random random;

    @BeforeEach
    void setUp(){
        board = new ViewBoard("testPlayer");
        printBoardUI = new PrintBoardUI(board);
        random = new Random();

        randomScoresTest(); // set random scores
        board.addPlayer("Player2");
        board.addPlayer("Player3");
        board.addPlayer("Player4");
        board.getAllPlayerHands().forEach(h->h.setColor(getRandomAvailableColor(board)));
        // set random colors for all players

        // set turns
        int t = random.nextInt(4)+1;
        for (ViewHand hand : board.getAllPlayerHands()) {
            hand.setTurn(t);
            if(t >= 4) t=1;
            else t++;
        }

        //randomize connection status
        board.getOpponents().forEach(h -> h.setConnected(random.nextBoolean()));

        board.getResourceCardDeck().setTopCard(getRandomResourceCard());
        board.getResourceCardDeck().setFirstRevealed(getRandomResourceCard());
        board.getResourceCardDeck().setSecondRevealed(getRandomResourceCard());

        board.getGoldCardDeck().setTopCard(getRandomGoldCard());
        board.getGoldCardDeck().setFirstRevealed(getRandomGoldCard());
        board.getGoldCardDeck().setSecondRevealed(getRandomGoldCard());

        board.getObjectiveCardDeck().setTopCard(getRandomObjectiveCard(random.nextBoolean()));
        board.getObjectiveCardDeck().setFirstRevealed(getRandomObjectiveCard(random.nextBoolean()));
        board.getObjectiveCardDeck().setSecondRevealed(getRandomObjectiveCard(random.nextBoolean()));
    }

    @AfterEach
    void printUI(){
        printBoardUI.display();
        System.out.println("End of Board test print.\n\n");
    }

    @Test
    void fullBoardTest(){
        // board already full
    }
    @Test
    void emptyBoardTest(){
        board.getResourceCardDeck().setTopCard(null);
        board.getResourceCardDeck().setFirstRevealed(null);
        board.getResourceCardDeck().setSecondRevealed(null);

        board.getGoldCardDeck().setTopCard(null);
        board.getGoldCardDeck().setFirstRevealed(null);
        board.getGoldCardDeck().setSecondRevealed(null);

        board.getObjectiveCardDeck().setTopCard(null);
        board.getObjectiveCardDeck().setFirstRevealed(null);
        board.getObjectiveCardDeck().setSecondRevealed(null);
    }
    @Test
    void printTopRowEmptyTest(){
        board.getResourceCardDeck().setTopCard(null);
        board.getGoldCardDeck().setTopCard(null);
        board.getObjectiveCardDeck().setTopCard(null);
    }
    @Test
    void printFirstRowEmptyTest(){
        board.getResourceCardDeck().setFirstRevealed(null);
        board.getGoldCardDeck().setFirstRevealed(null);
        board.getObjectiveCardDeck().setFirstRevealed(null);
    }
    @Test
    void printSecondRowEmptyTest(){
        board.getResourceCardDeck().setSecondRevealed(null);
        board.getGoldCardDeck().setSecondRevealed(null);
        board.getObjectiveCardDeck().setSecondRevealed(null);
    }

    @ParameterizedTest
    @ValueSource(ints={0,1,2,5,8,15,20,23,28,29})
    void allPlayersOnSameScoreTest(int score){
        board.getAllPlayerHands().forEach(h->board.setScore(h.getNickname(), score));
    }
    @Test
    void randomScoresTest(){
        board.getAllPlayerHands().forEach(h->board.setScore(h.getNickname(), getRandomScore()));
    }
    @Test
    void endgameTest(){
        board.getAllPlayerHands().forEach(h->board.setScore(h.getNickname(), getRandomScore()));
        board.setScore(
                board.getAllPlayerHands().stream().findAny().orElseThrow().getNickname(),
                random.nextInt(10)+20
        );
    }
}
