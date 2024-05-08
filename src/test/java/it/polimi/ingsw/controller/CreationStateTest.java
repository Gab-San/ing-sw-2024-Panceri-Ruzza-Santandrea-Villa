package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Point;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.PlayerColor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class CreationStateTest extends BaseBoardControllerTest {
    CreationState creationState;
    private Class<? extends GameState> nextStateClass;

    @BeforeEach
    public void setUpCreationState(){
        creationState = new CreationState(board);
        nextStateClass = JoinState.class;
    }

    @Test
    public void joinTest() {
        //TODO: FIXME: virtual client is null, must be added assertion with VirtualClient != null
        assertThrows(IllegalStateException.class, () -> creationState.join(firstPlayer.getNickname(), null), "Join doesn't throw IllegalStateException with client==null");
    }

    @ParameterizedTest
    @ValueSource(ints={0,1,2,3,4,5,6})
    public void setNumOfPlayersTest(int i) {
        assertEquals(GamePhase.SNOFP, board.getGamePhase());
        assertEquals(creationState.board, this.board, "The board changed");
        assertThrows(IllegalArgumentException.class, () -> creationState.setNumOfPlayers("definitelyNotRight", i),"Does not throw IllegalArgumentException with nickname not inside the game");
        if (i > 4 || i < 2) {
            assertThrows(IllegalArgumentException.class, () -> creationState.setNumOfPlayers(firstPlayer.getNickname(), i), "Does not throw IllegalArgumentException when i=" + i);
        } else {
            GameState nextGameState = creationState.setNumOfPlayers(firstPlayer.getNickname(), i);
            assertEquals(nextGameState.getClass(), nextStateClass, "Test with i=" + i + " wrong next state: " +
                    "it is expected" + nextStateClass + ", but it is " + nextGameState.getClass());
            assertSame(nextGameState.board, this.board, "Test with i=" + i + " wrong board in nextGameState: " +
                    "it is expected" + board + ", but it is " + nextGameState.board);
        }
    }

    @Test
    public void placeStartingCardTest() {
        assertThrows(IllegalStateException.class, () -> creationState.placeStartingCard("definitelyNotRight", true), "PlaceStartingCard doesn't throw IllegalStateException with wrong nickname");
        assertThrows(IllegalStateException.class, () -> creationState.placeStartingCard(firstPlayer.getNickname(), true), "PlaceStartingCard doesn't throw IllegalStateException with placeOnFront==true");
        assertThrows(IllegalStateException.class, () -> creationState.placeStartingCard(firstPlayer.getNickname(), false), "PlaceStartingCard doesn't throw IllegalStateException with placeOnFront==false");
    }

    @Test
    public void chooseYourColorTest() {
        assertThrows(IllegalStateException.class, () -> creationState.chooseYourColor("definitelyNotRight", PlayerColor.BLUE),"ChooseYourColor doesn't throw IllegalStateException with wrong nickname");
        assertThrows(IllegalStateException.class, () -> creationState.chooseYourColor(firstPlayer.getNickname(), PlayerColor.BLUE),"ChooseYourColor doesn't throw IllegalStateException with color==BLUE");
        assertThrows(IllegalStateException.class, () -> creationState.chooseYourColor(firstPlayer.getNickname(), PlayerColor.RED),"ChooseYourColor doesn't throw IllegalStateException with color==RED");
        assertThrows(IllegalStateException.class, () -> creationState.chooseYourColor(firstPlayer.getNickname(), PlayerColor.GREEN),"ChooseYourColor doesn't throw IllegalStateException with color==GREEN");
        assertThrows(IllegalStateException.class, () -> creationState.chooseYourColor(firstPlayer.getNickname(), PlayerColor.YELLOW),"ChooseYourColor doesn't throw IllegalStateException with color==YELLOW");
    }

    @Test
    public void chooseSecretObjectiveTest() {
        assertThrows(IllegalStateException.class, () -> creationState.chooseSecretObjective("definitelyNotRight", 0), "ChooseSecretObjective doesn't throw IllegalStateException with wrong nickname");
        assertThrows(IllegalStateException.class, () -> creationState.chooseSecretObjective(firstPlayer.getNickname(), 0), "ChooseSecretObjective doesn't throw IllegalStateException with choice==0");
        assertThrows(IllegalStateException.class, () -> creationState.chooseSecretObjective(firstPlayer.getNickname(), 1), "ChooseSecretObjective doesn't throw IllegalStateException with choice==1");
    }

    @Test
    public void placeCardTest() {
        assertThrows(IllegalStateException.class, () -> creationState.placeCard("definitelyNotRight", "r0", new Point(0, 0), CornerDirection.TR, true), "PlaceCard doesn't throw IllegalStateException with wrong nickname");
        assertThrows(IllegalStateException.class, () -> creationState.placeCard(firstPlayer.getNickname(), "r0", new Point(0, 0), CornerDirection.TR, true),"PlaceCard doesn't throw IllegalStateException");
        assertThrows(IllegalStateException.class, () -> creationState.placeCard(firstPlayer.getNickname(), "r2", new Point(1, 0), CornerDirection.TL, false),"PlaceCard doesn't throw IllegalStateException");
        assertThrows(IllegalStateException.class, () -> creationState.placeCard(firstPlayer.getNickname(), "g22", new Point(0, 1), CornerDirection.BL, true),"PlaceCard doesn't throw IllegalStateException");
        assertThrows(IllegalStateException.class, () -> creationState.placeCard(firstPlayer.getNickname(), "g8", new Point(1, 1), CornerDirection.BL, false),"PlaceCard doesn't throw IllegalStateException");
    }

    @Test
    public void drawTest() {
        assertThrows(IllegalStateException.class, () -> creationState.draw("definitelyNotRight", 'r', 0), "Draw doesn't throw IllegalStateException with wrong nickname");
        assertThrows(IllegalStateException.class, () -> creationState.draw(firstPlayer.getNickname(), 'r', 0), "Draw doesn't throw IllegalStateException with deckFrom==r and cardPos==0");
        assertThrows(IllegalStateException.class, () -> creationState.draw(firstPlayer.getNickname(), 'r', 1), "Draw doesn't throw IllegalStateException with deckFrom==r and cardPos==1");
        assertThrows(IllegalStateException.class, () -> creationState.draw(firstPlayer.getNickname(), 'r', 2), "Draw doesn't throw IllegalStateException with deckFrom==r and cardPos==2");
        assertThrows(IllegalStateException.class, () -> creationState.draw(firstPlayer.getNickname(), 'g', 0), "Draw doesn't throw IllegalStateException with deckFrom==g and cardPos==0");
        assertThrows(IllegalStateException.class, () -> creationState.draw(firstPlayer.getNickname(), 'g', 1), "Draw doesn't throw IllegalStateException with deckFrom==g and cardPos==1");
        assertThrows(IllegalStateException.class, () -> creationState.draw(firstPlayer.getNickname(), 'g', 2),"Draw doesn't throw IllegalStateException with deckFrom==g and cardPos==2");
    }

    @Test
    public void startGameTest() {
        assertThrows(IllegalStateException.class, () -> creationState.startGame("definitelyNotRight", 2), "StartGame doesn't throw IllegalStateException with wrong nickname");
        assertThrows(IllegalStateException.class, () -> creationState.startGame(firstPlayer.getNickname(), 2), "StartGame doesn't throw IllegalStateException with numOfPlayers==2");
        assertThrows(IllegalStateException.class, () -> creationState.startGame(firstPlayer.getNickname(), 3),"StartGame doesn't throw IllegalStateException with numOfPlayers==3");
        assertThrows(IllegalStateException.class, () -> creationState.startGame(firstPlayer.getNickname(), 4),"StartGame doesn't throw IllegalStateException with numOfPlayers==4");
    }
}


