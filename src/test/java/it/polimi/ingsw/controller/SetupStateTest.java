package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SetupStateTest extends BaseBoardControllerTest{
    GameState setupState;

    public void setUp(int numOfPlayers){
        setupState = new JoinStateTest().joinUntilNextState(numOfPlayers);
    }
    public GameState advanceToNextState(int numOfPlayers){
        setUp(numOfPlayers);

    }

    @Test
    public void joinTest() {
    }

    @Test
    public void setNumOfPlayersTest() {

    }

    @Test
    public void placeStartingCardTest() {

    }

    @Test
    public void chooseYourColorTest() {

    }

    @Test
    public void chooseSecretObjectiveTest() {

    }

    @Test
    public void placeCardTest() {

    }

    @Test
    public void drawTest() {

    }

    @Test
    public void startGameTest() {

    }
}
