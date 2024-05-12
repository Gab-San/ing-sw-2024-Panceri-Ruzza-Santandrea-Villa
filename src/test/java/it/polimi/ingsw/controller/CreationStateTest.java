package it.polimi.ingsw.controller;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.controller.testclass.PuppetClient;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.PlayerColor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class CreationStateTest {
    private BoardController controller;
    private Class<? extends GameState> nextStateClass;
    private Board board;
    private final String playerNickname = "Flavio";
    @BeforeEach
    public void setUpcontroller(){
        controller = new BoardController("Gamba Game");
        nextStateClass = JoinState.class;
        board = controller.getGameState().board;
        assertEquals(controller.getGameState().board,board , "The board changed");
        controller.join(playerNickname, new PuppetClient());
    }

    @Test
    public void joinTest() {
        assertThrows(IllegalStateException.class, () -> controller.join("Giovanni", new PuppetClient()), "Join doesn't throw IllegalStateException with client==null");
    }

    @ParameterizedTest
    @ValueSource(ints={0,1,2,3,4,5,6})
    public void setNumOfPlayersTest(int i) {
        assertEquals(GamePhase.SNP, board.getGamePhase());
        assertThrows(IllegalArgumentException.class, () -> controller.setNumOfPlayers("definitelyNotRight", i),"Does not throw IllegalArgumentException with nickname not inside the game");
        if (i < 2|| i > 4) {
            assertThrows(IllegalArgumentException.class, () -> controller.setNumOfPlayers(playerNickname, i), "Does not throw IllegalArgumentException when i=" + i);
        } else {
            controller.setNumOfPlayers(playerNickname, i);
            // Set Num Of Player makes a transition
            GameState nextGameState = controller.getGameState();
            assertEquals(nextGameState.board, board, "The board changed");
            assertEquals(nextGameState.getClass(), nextStateClass, "Test with i=" + i + " wrong next state: " +
                    "it is expected" + nextStateClass + ", but it is " + nextGameState.getClass());
            assertSame(nextGameState.board, board, "Test with i=" + i + " wrong board in nextGameState: " +
                    "it is expected" + board + ", but it is " + nextGameState.board);
            assertEquals(GamePhase.JOIN, board.getGamePhase(), "wrong phase at the end");
        }
    }

    @Test
    public void placeStartingCardTest() {
        assertThrows(IllegalStateException.class, () -> controller.placeStartingCard("definitelyNotRight", true), "PlaceStartingCard doesn't throw IllegalStateException with wrong nickname");
        assertThrows(IllegalStateException.class, () -> controller.placeStartingCard(playerNickname, true), "PlaceStartingCard doesn't throw IllegalStateException with placeOnFront==true");
        assertThrows(IllegalStateException.class, () -> controller.placeStartingCard(playerNickname, false), "PlaceStartingCard doesn't throw IllegalStateException with placeOnFront==false");
    }

    @Test
    public void chooseYourColorTest() {
        assertThrows(IllegalStateException.class, () -> controller.chooseYourColor("definitelyNotRight", PlayerColor.BLUE),"ChooseYourColor doesn't throw IllegalStateException with wrong nickname");
        assertThrows(IllegalStateException.class, () -> controller.chooseYourColor(playerNickname, PlayerColor.BLUE),"ChooseYourColor doesn't throw IllegalStateException with color==BLUE");
        assertThrows(IllegalStateException.class, () -> controller.chooseYourColor(playerNickname, PlayerColor.RED),"ChooseYourColor doesn't throw IllegalStateException with color==RED");
        assertThrows(IllegalStateException.class, () -> controller.chooseYourColor(playerNickname, PlayerColor.GREEN),"ChooseYourColor doesn't throw IllegalStateException with color==GREEN");
        assertThrows(IllegalStateException.class, () -> controller.chooseYourColor(playerNickname, PlayerColor.YELLOW),"ChooseYourColor doesn't throw IllegalStateException with color==YELLOW");
    }

    @Test
    public void chooseSecretObjectiveTest() {
        assertThrows(IllegalStateException.class, () -> controller.chooseSecretObjective("definitelyNotRight", 0), "ChooseSecretObjective doesn't throw IllegalStateException with wrong nickname");
        assertThrows(IllegalStateException.class, () -> controller.chooseSecretObjective(playerNickname, 0), "ChooseSecretObjective doesn't throw IllegalStateException with choice==0");
        assertThrows(IllegalStateException.class, () -> controller.chooseSecretObjective(playerNickname, 1), "ChooseSecretObjective doesn't throw IllegalStateException with choice==1");
    }

    @Test
    public void placeCardTest() {
        assertThrows(IllegalStateException.class, () -> controller.placeCard("definitelyNotRight", "r0", new Point(0, 0), CornerDirection.TR, true), "PlaceCard doesn't throw IllegalStateException with wrong nickname");
        assertThrows(IllegalStateException.class, () -> controller.placeCard(playerNickname, "r0", new Point(0, 0), CornerDirection.TR, true),"PlaceCard doesn't throw IllegalStateException");
        assertThrows(IllegalStateException.class, () -> controller.placeCard(playerNickname, "r2", new Point(1, 0), CornerDirection.TL, false),"PlaceCard doesn't throw IllegalStateException");
        assertThrows(IllegalStateException.class, () -> controller.placeCard(playerNickname, "g22", new Point(0, 1), CornerDirection.BL, true),"PlaceCard doesn't throw IllegalStateException");
        assertThrows(IllegalStateException.class, () -> controller.placeCard(playerNickname, "g8", new Point(1, 1), CornerDirection.BL, false),"PlaceCard doesn't throw IllegalStateException");
    }

    @Test
    public void drawTest() {
        assertThrows(IllegalStateException.class, () -> controller.draw("definitelyNotRight", 'r', 0), "Draw doesn't throw IllegalStateException with wrong nickname");
        assertThrows(IllegalStateException.class, () -> controller.draw(playerNickname, 'r', 0), "Draw doesn't throw IllegalStateException with deckFrom==r and cardPos==0");
        assertThrows(IllegalStateException.class, () -> controller.draw(playerNickname, 'r', 1), "Draw doesn't throw IllegalStateException with deckFrom==r and cardPos==1");
        assertThrows(IllegalStateException.class, () -> controller.draw(playerNickname, 'r', 2), "Draw doesn't throw IllegalStateException with deckFrom==r and cardPos==2");
        assertThrows(IllegalStateException.class, () -> controller.draw(playerNickname, 'g', 0), "Draw doesn't throw IllegalStateException with deckFrom==g and cardPos==0");
        assertThrows(IllegalStateException.class, () -> controller.draw(playerNickname, 'g', 1), "Draw doesn't throw IllegalStateException with deckFrom==g and cardPos==1");
        assertThrows(IllegalStateException.class, () -> controller.draw(playerNickname, 'g', 2),"Draw doesn't throw IllegalStateException with deckFrom==g and cardPos==2");
    }

    @Test
    public void startGameTest() {
        assertThrows(IllegalStateException.class, () -> controller.startGame("definitelyNotRight", 2), "StartGame doesn't throw IllegalStateException with wrong nickname");
        assertThrows(IllegalStateException.class, () -> controller.startGame(playerNickname, 2), "StartGame doesn't throw IllegalStateException with numOfPlayers==2");
        assertThrows(IllegalStateException.class, () -> controller.startGame(playerNickname, 3),"StartGame doesn't throw IllegalStateException with numOfPlayers==3");
        assertThrows(IllegalStateException.class, () -> controller.startGame(playerNickname, 4),"StartGame doesn't throw IllegalStateException with numOfPlayers==4");
    }
}


