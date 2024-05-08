package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enums.GamePhase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        nextGS=new SetupState(new Board("ignore"));
    }

    @Test
    public void SetNumOfPlayersTest(){
        assert (board.getGamePhase().equals(GamePhase.SNOFP));
        String nextClass=nextGS.getClass().toString();
        GameState nextGameState=null;
        for(int i=0; i<6;i++) {
            if(i>4 || i<2) {
                int finalI = i;
                assertThrows(IllegalArgumentException.class, ()->creationState.setNumOfPlayers(firstPlayer.getNickname(), finalI));
            }
            else {
                nextGameState=creationState.setNumOfPlayers(firstPlayer.getNickname(), i);
                assertEquals(nextGameState.getClass().toString(), nextClass, "Test with i=" + i + " wrong next state: " +
                        "it is expected" + nextClass + ", but it is " + nextGameState.getClass().toString());
                assertSame(nextGameState.board, this.board, "Test with i=" + i + " wrong board in nextGameState: " +
                        "it is expected" + board + ", but it is " + nextGameState.board);
            }

        }


    }
}
