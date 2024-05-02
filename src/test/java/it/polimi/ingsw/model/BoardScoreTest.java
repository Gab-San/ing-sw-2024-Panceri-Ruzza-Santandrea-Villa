package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BoardScoreTest extends BaseBoardTest{
    @Test
    public void checkEndGameTest(){
        joinPlayers(4);
        assertFalse(board.checkEndgame());
        board.addScore(players[2], 10);
        assertFalse(board.checkEndgame());
        board.addScore(players[0], 10);
        board.addScore(players[1], 10);
        board.addScore(players[3], 10);
        assertFalse(board.checkEndgame());
        board.addScore(players[2], 15);  // player2 has a score of 25
        assertTrue(board.checkEndgame());
    }
    @Test
    public void scoreboardTest(){
        joinPlayers(4);
        Map<Player, Integer> scoreboard = board.getScoreboard();
        Runnable printAll = ()-> scoreboard.forEach((p,s)->System.out.println(p.getNickname() + ": " + s));

        for (Player p : players) {
            assertEquals(0, scoreboard.get(p));
        }

        board.setScore(players[2], 15);
        assertEquals(15, scoreboard.get(players[2]));

        board.setScore(players[1], 15);
        assertEquals(15, scoreboard.get(players[1]));

        board.setScore(players[2], 23);
        assertEquals(23, scoreboard.get(players[2]));

        board.addScore(players[0], 15);
        assertEquals(15, scoreboard.get(players[0]));

        board.addScore(players[0], 5);
        assertEquals(20, scoreboard.get(players[0]));
        printAll.run();
    }
}
