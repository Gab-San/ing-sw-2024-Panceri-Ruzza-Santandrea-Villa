package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enums.GamePhase;
import org.junit.jupiter.api.BeforeEach;
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
    public void SetNumOfPlayersTest(int i){
        assert (board.getGamePhase().equals(GamePhase.SNOFP));
        Class<? extends GameState> nextClass=nextGS.getClass();
        GameState nextGameState;
        if(i<2 || i>4) {
            assertThrows(IllegalArgumentException.class, ()->creationState.setNumOfPlayers(firstPlayer.getNickname(), i));
        }
        else {
            nextGameState=creationState.setNumOfPlayers(firstPlayer.getNickname(), i);
            assertEquals(nextGameState.getClass(), nextClass, "Test with i=" + i + " wrong next state: " +
                    "it is expected " + nextClass + ", but it is " + nextGameState.getClass());
            assertSame(nextGameState.board, this.board, "Test with i=" + i + " wrong board in nextGameState: " +
                    "it is expected" + board + ", but it is " + nextGameState.board);
        }
    }
}
