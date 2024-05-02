package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class BoardNoDeckTest {
    static Board board;
    Player[] players;

    @BeforeAll
    public static void createBoard(){
        board = new Board("testGame");
    }
    @BeforeEach
    public void setUp(){
        players = new Player[4];
        board.setCurrentTurn(1);
        board.getPlayerAreas().clear();
        board.getScoreboard().clear();
        board.getPlayerDeadlocks().clear();
    }
    @ParameterizedTest
    @ValueSource( ints = {1,2,3,4} )
    public void joinPlayers(int num) throws IllegalStateException{
        for (int i = 0; i < num; i++) {
            players[i] = new Player("player"+(i+1), null, i+1);
            Player p = players[i];
            assertDoesNotThrow(()->board.addPlayer(p));
        }
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
        players[3] = new Player("player4");
        assertDoesNotThrow(()->board.addPlayer(players[3]));
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

    @Test
    public void playerDeadlockTest(){
        //TODO: assert correct identification of deadlocks
        // assert correct skipping of deadlocked player's turn
        // assert correct return value for the game continuing and the game ending due to deadlocks
        //maybe do this on a 2-player game for simplicity
    }
}
