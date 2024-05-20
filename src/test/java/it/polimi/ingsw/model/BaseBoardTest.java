package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BaseBoardTest {
    Board board;
    Player[] players;

    @BeforeEach
    public void setUp(){
        board = new Board();
        players = null;
        board.setCurrentTurn(1);
    }
    protected void joinPlayers(int num) throws IllegalStateException{
        players = new Player[num];
        for (int i = 0; i < num; i++) {
            players[i] = new Player("player"+(i+1));
            players[i].setTurn(i+1);
            board.addPlayer(players[i]);
        }
    }
}
