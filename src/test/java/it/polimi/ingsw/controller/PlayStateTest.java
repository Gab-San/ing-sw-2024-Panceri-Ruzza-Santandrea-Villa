package it.polimi.ingsw.controller;

import org.junit.jupiter.api.Test;

public class PlayStateTest extends BaseBoardControllerTest {
    GameState playState;

    public void setUp(int numOfPlayers){
        GameState playState = new SetupStateTest().advanceToPlayState(numOfPlayers);
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
