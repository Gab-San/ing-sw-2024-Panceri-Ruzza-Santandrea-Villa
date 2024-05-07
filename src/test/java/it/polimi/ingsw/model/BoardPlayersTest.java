package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.StartingCard;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoardPlayersTest extends BaseBoardTest {
    @ParameterizedTest
    @ValueSource( ints = {1,2,3,4} )
    public void joinNumsTest(int num){
        joinPlayers(num);
    }
    @Test
    public void joinFailureTooManyPlayersTest(){
        joinPlayers(4);
        assertThrows(IllegalStateException.class, ()->board.addPlayer(new Player("player5")));
    }
    @Test
    public void joinFailureDuplicateNicknameTest(){
        joinPlayers(3);
        assertThrows(IllegalStateException.class,
                ()->board.addPlayer(new Player("player2")),
                "Shouldn't be able to add duplicate nickname"
        );
        assertDoesNotThrow(()->board.addPlayer(new Player("player4")));
    }
    @Test
    public void getPlayersOrderingTest(){
        joinPlayers(4);
        List<Player> playersByTurn = board.getPlayersByTurn();
        board.addScore(board.getPlayerByNickname("player1"), 10);
        board.addScore(board.getPlayerByNickname("player2"), 1);
        board.addScore(board.getPlayerByNickname("player4"), 15);
        List<Player> playersByScore = board.getPlayersByScore();

        for (int i = 0; i < playersByTurn.size()-1; i++) {
            assertTrue(playersByTurn.get(i).getTurn() < playersByTurn.get(i+1).getTurn());
            assertTrue(board.getScoreboard().get(playersByScore.get(i)) < board.getScoreboard().get(playersByScore.get(i+1)));
            assertSame(players[i], playersByTurn.get(i));
        }
    }
}
