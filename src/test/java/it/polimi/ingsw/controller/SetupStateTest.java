package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SetupStateTest extends BaseBoardControllerTest{
    GameState setupState;

    public void setUp(int numOfPlayers){
        setupState = new JoinStateTest().joinUntilSetupState(numOfPlayers);
    }
    public GameState advanceToPlayState(int numOfPlayers){
        setUp(numOfPlayers);
        List<Player> players = board.getPlayersByTurn();
        for(Player p : players){
            setupState.placeStartingCard(p.getNickname(), false);
        }
        for(Player p : players){
            assertDoesNotThrow(()->setupState.chooseYourColor(p.getNickname(), board.getRandomAvailableColor()));
        }
        GameState nextState = null;
        for(Player p : players){
            nextState = setupState.chooseSecretObjective(p.getNickname(), 1);
        }
        assertNotNull(nextState);
        assertEquals(PlayState.class, nextState.getClass());
        return nextState;
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
