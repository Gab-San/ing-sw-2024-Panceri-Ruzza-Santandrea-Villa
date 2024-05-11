package it.polimi.ingsw.controller;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.PlayerColor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

public class JoinStateTest extends BaseBoardControllerTest {
    GameState joinState;
/*
    public JoinStateTest(int i){
        CreationStateTest creationStateTest=new CreationStateTest();
        joinState=creationStateTest.upToJoinState(i);
    }*/

    private void setUp(int numOfPlayers){
        joinState = new CreationState(board).setNumOfPlayers(firstPlayer.getNickname(), numOfPlayers);
    }

    @ParameterizedTest
    @ValueSource(ints={2,3,4})
    public void joinUntilNextStateTest(int numOfPlayers){
        joinUntilSetupState(numOfPlayers);
    }

    protected GameState joinUntilSetupState(int numOfPlayers){
        setUp(numOfPlayers);
        assertEquals(JoinState.class, joinState.getClass(), "bho");
        GameState nextGS=null;
        for(int j = 2; j <= numOfPlayers; j++){
            nextGS = joinState.join("Player"+j, null);
        }
        assertNotNull(nextGS);
        assertEquals(SetupState.class, nextGS.getClass());
        assertEquals(board.getGamePhase(), GamePhase.PSCP);
        return nextGS;
    }

    @Test
    public void joinTest() {
        setUp(4);
        //TODO: FIXME: virtual client is null, must be added assertion with VirtualClient != null
        assertThrows(IllegalStateException.class, () -> joinState.join(firstPlayer.getNickname(), null), "Join doesn't throw IllegalStateException with client==null");
        // only test join failures here
    }

    @ParameterizedTest
    @ValueSource(ints={0,1,2,3,4,5,6})
    public void setNumOfPlayersTest(int i) {
        setUp(4);
        assertThrows(IllegalStateException.class, () -> joinState.setNumOfPlayers("definitelyNotRight", i), "SetNumOfPlayers doesn't throw IllegalStateException wrong nickname");
        assertThrows(IllegalStateException.class, () -> joinState.setNumOfPlayers(firstPlayer.getNickname(), i), "SetNumOfPlayers doesn't throw IllegalStateException with i="+i);
    }

    @Test
    public void placeStartingCardTest() {
        setUp(4);
        assertThrows(IllegalStateException.class, () -> joinState.placeStartingCard("definitelyNotRight", true), "PlaceStartingCard doesn't throw IllegalStateException with wrong nickname");
        assertThrows(IllegalStateException.class, () -> joinState.placeStartingCard(firstPlayer.getNickname(), true), "PlaceStartingCard doesn't throw IllegalStateException with placeOnFront==true");
        assertThrows(IllegalStateException.class, () -> joinState.placeStartingCard(firstPlayer.getNickname(), false), "PlaceStartingCard doesn't throw IllegalStateException with placeOnFront==false");
    }

    @Test
    public void chooseYourColorTest() {
        setUp(4);
        assertThrows(IllegalStateException.class, () -> joinState.chooseYourColor("definitelyNotRight", PlayerColor.BLUE),"ChooseYourColor doesn't throw IllegalStateException with wrong nickname");
        assertThrows(IllegalStateException.class, () -> joinState.chooseYourColor(firstPlayer.getNickname(), PlayerColor.BLUE),"ChooseYourColor doesn't throw IllegalStateException with color==BLUE");
        assertThrows(IllegalStateException.class, () -> joinState.chooseYourColor(firstPlayer.getNickname(), PlayerColor.RED),"ChooseYourColor doesn't throw IllegalStateException with color==RED");
        assertThrows(IllegalStateException.class, () -> joinState.chooseYourColor(firstPlayer.getNickname(), PlayerColor.GREEN),"ChooseYourColor doesn't throw IllegalStateException with color==GREEN");
        assertThrows(IllegalStateException.class, () -> joinState.chooseYourColor(firstPlayer.getNickname(), PlayerColor.YELLOW),"ChooseYourColor doesn't throw IllegalStateException with color==YELLOW");
    }

    @Test
    public void chooseSecretObjectiveTest() {
        setUp(4);
        assertThrows(IllegalStateException.class, () -> joinState.chooseSecretObjective("definitelyNotRight", 0), "ChooseSecretObjective doesn't throw IllegalStateException with wrong nickname");
        assertThrows(IllegalStateException.class, () -> joinState.chooseSecretObjective(firstPlayer.getNickname(), 0), "ChooseSecretObjective doesn't throw IllegalStateException with choice==0");
        assertThrows(IllegalStateException.class, () -> joinState.chooseSecretObjective(firstPlayer.getNickname(), 1), "ChooseSecretObjective doesn't throw IllegalStateException with choice==1");
    }

    @Test
    public void placeCardTest() {
        setUp(4);
        assertThrows(IllegalStateException.class, () -> joinState.placeCard("definitelyNotRight", "r0", new Point(0, 0), CornerDirection.TR, true), "PlaceCard doesn't throw IllegalStateException with wrong nickname");
        assertThrows(IllegalStateException.class, () -> joinState.placeCard(firstPlayer.getNickname(), "r0", new Point(0, 0), CornerDirection.TR, true),"PlaceCard doesn't throw IllegalStateException");
        assertThrows(IllegalStateException.class, () -> joinState.placeCard(firstPlayer.getNickname(), "r2", new Point(1, 0), CornerDirection.TL, false),"PlaceCard doesn't throw IllegalStateException");
        assertThrows(IllegalStateException.class, () -> joinState.placeCard(firstPlayer.getNickname(), "g22", new Point(0, 1), CornerDirection.BL, true),"PlaceCard doesn't throw IllegalStateException");
        assertThrows(IllegalStateException.class, () -> joinState.placeCard(firstPlayer.getNickname(), "g8", new Point(1, 1), CornerDirection.BL, false),"PlaceCard doesn't throw IllegalStateException");
    }

    @Test
    public void drawTest() {
        setUp(4);
        assertThrows(IllegalStateException.class, () -> joinState.draw("definitelyNotRight", 'r', 0), "Draw doesn't throw IllegalStateException with wrong nickname");
        assertThrows(IllegalStateException.class, () -> joinState.draw(firstPlayer.getNickname(), 'r', 0), "Draw doesn't throw IllegalStateException with deckFrom==r and cardPos==0");
        assertThrows(IllegalStateException.class, () -> joinState.draw(firstPlayer.getNickname(), 'r', 1), "Draw doesn't throw IllegalStateException with deckFrom==r and cardPos==1");
        assertThrows(IllegalStateException.class, () -> joinState.draw(firstPlayer.getNickname(), 'r', 2), "Draw doesn't throw IllegalStateException with deckFrom==r and cardPos==2");
        assertThrows(IllegalStateException.class, () -> joinState.draw(firstPlayer.getNickname(), 'g', 0), "Draw doesn't throw IllegalStateException with deckFrom==g and cardPos==0");
        assertThrows(IllegalStateException.class, () -> joinState.draw(firstPlayer.getNickname(), 'g', 1), "Draw doesn't throw IllegalStateException with deckFrom==g and cardPos==1");
        assertThrows(IllegalStateException.class, () -> joinState.draw(firstPlayer.getNickname(), 'g', 2),"Draw doesn't throw IllegalStateException with deckFrom==g and cardPos==2");
    }

    @Test
    public void startGameTest() {
        setUp(4);
        assertThrows(IllegalStateException.class, () -> joinState.startGame("definitelyNotRight", 2), "StartGame doesn't throw IllegalStateException with wrong nickname");
        assertThrows(IllegalStateException.class, () -> joinState.startGame(firstPlayer.getNickname(), 2), "StartGame doesn't throw IllegalStateException with numOfPlayers==2");
        assertThrows(IllegalStateException.class, () -> joinState.startGame(firstPlayer.getNickname(), 3),"StartGame doesn't throw IllegalStateException with numOfPlayers==3");
        assertThrows(IllegalStateException.class, () -> joinState.startGame(firstPlayer.getNickname(), 4),"StartGame doesn't throw IllegalStateException with numOfPlayers==4");
    }
}
