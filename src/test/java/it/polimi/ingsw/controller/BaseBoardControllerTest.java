package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseBoardControllerTest {
    BoardController boardController;
    Board board;
    Player firstPlayer;

    @BeforeEach
    public void setUpBase(){
        firstPlayer = new Player("Player1");
        board = new Board("TestCreationState", firstPlayer);
    }

    public void disconnectTest(){
        //TODO: [Ale] wait for network, implementation may change
    }
}
