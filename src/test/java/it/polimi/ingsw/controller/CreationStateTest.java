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

public class CreationStateTest {
    CreationState creationState;
    Board board;
    Player firstPlayer;
    GameState nextGS;
    @BeforeEach
    public void setUp(){
        firstPlayer=new Player("Creator");
        board=new Board("TestCreationState", firstPlayer);
        creationState=new CreationState(board);
        nextGS=new JoinState(new Board("ignore"));
    }

    @ParameterizedTest
    @ValueSource(ints={0,1,2,3,4,5,6})
    public void SetNumOfPlayersTest(int i) {
        assert (board.getGamePhase().equals(GamePhase.SNOFP));
        Class<? extends GameState> nextClass=nextGS.getClass();
        GameState nextGameState;
        if (i > 4 || i < 2) {
            assertThrows(IllegalArgumentException.class, () -> creationState.setNumOfPlayers(firstPlayer.getNickname(), i));
        } else {
            try {
                nextGameState = creationState.setNumOfPlayers(firstPlayer.getNickname(), i);
                assertEquals(nextGameState.getClass(), nextClass, "Test with i=" + i + " wrong next state: " +
                        "it is expected" + nextClass + ", but it is " + nextGameState.getClass());
                assertSame(nextGameState.board, this.board, "Test with i=" + i + " wrong board in nextGameState: " +
                        "it is expected" + board + ", but it is " + nextGameState.board);
            } catch (Exception e) {
                fail("Some exception was raised: " + e.getMessage());
            }
        }
    }

    @Test
    public void ExeptionsTests(){
        assertThrows(IllegalStateException.class, () -> creationState.placeStartingCard(firstPlayer.getNickname(), true));
        assertThrows(IllegalStateException.class, () -> creationState.chooseYourColor(firstPlayer.getNickname(), PlayerColor.BLUE));
        assertThrows(IllegalStateException.class, () -> creationState.chooseYourColor(firstPlayer.getNickname(), PlayerColor.RED));
        assertThrows(IllegalStateException.class, () -> creationState.chooseYourColor(firstPlayer.getNickname(), PlayerColor.GREEN));
        assertThrows(IllegalStateException.class, () -> creationState.chooseYourColor(firstPlayer.getNickname(), PlayerColor.YELLOW));
        assertThrows(IllegalStateException.class, () -> creationState.chooseSecretObjective(firstPlayer.getNickname(), 0));
        assertThrows(IllegalStateException.class, () -> creationState.chooseSecretObjective(firstPlayer.getNickname(), 1));
        assertThrows(IllegalStateException.class, () -> creationState.draw(firstPlayer.getNickname(), 'r', 0));
        assertThrows(IllegalStateException.class, () -> creationState.draw(firstPlayer.getNickname(), 'r', 1));
        assertThrows(IllegalStateException.class, () -> creationState.draw(firstPlayer.getNickname(), 'r', 2));
        assertThrows(IllegalStateException.class, () -> creationState.draw(firstPlayer.getNickname(), 'g', 0));
        assertThrows(IllegalStateException.class, () -> creationState.draw(firstPlayer.getNickname(), 'g', 1));
        assertThrows(IllegalStateException.class, () -> creationState.draw(firstPlayer.getNickname(), 'g', 2));
        //TODO: FIXME: virtual client is null, must be added assertion with VirtualClient != null
        assertThrows(IllegalStateException.class, () -> creationState.join(firstPlayer.getNickname(), null));
        assertThrows(IllegalStateException.class, () -> creationState.startGame(firstPlayer.getNickname(), 2));
        assertThrows(IllegalStateException.class, () -> creationState.startGame(firstPlayer.getNickname(), 3));
        assertThrows(IllegalStateException.class, () -> creationState.startGame(firstPlayer.getNickname(), 4));
        assertThrows(IllegalStateException.class, () -> creationState.placeCard(firstPlayer.getNickname(), "r0", new Point(0, 0), CornerDirection.TR, true));
        assertThrows(IllegalStateException.class, () -> creationState.placeCard(firstPlayer.getNickname(), "r2", new Point(1, 0), CornerDirection.TL, false));
        assertThrows(IllegalStateException.class, () -> creationState.placeCard(firstPlayer.getNickname(), "g22", new Point(0, 1), CornerDirection.BL, true));
        assertThrows(IllegalStateException.class, () -> creationState.placeCard(firstPlayer.getNickname(), "g8", new Point(1, 1), CornerDirection.BL, false));
    }
}


