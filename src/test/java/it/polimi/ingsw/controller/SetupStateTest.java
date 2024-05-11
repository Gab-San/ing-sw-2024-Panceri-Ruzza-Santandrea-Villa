package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.Point;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.PlayerColor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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
        setUp(4);
        assertThrows(IllegalStateException.class, () -> setupState.join(firstPlayer.getNickname(), null), "Join doesn't throw IllegalStateException with client==null");
    }

    @ParameterizedTest
    @ValueSource(ints={0,1,2,3,4,5,6})
    public void setNumOfPlayersTest(int i) {
        setUp(4);
        assertThrows(IllegalStateException.class, () -> setupState.setNumOfPlayers("definitelyNotRight", i), "SetNumOfPlayers doesn't throw IllegalStateException wrong nickname");
        assertThrows(IllegalStateException.class, () -> setupState.setNumOfPlayers(firstPlayer.getNickname(), i), "SetNumOfPlayers doesn't throw IllegalStateException with i="+i);
    }

    @Test
    public void placeStartingCardTest() {
        setUp(4);
        //assertThrows(NullPointerException.class, );
        assertThrows(IllegalStateException.class, () -> setupState.placeStartingCard("definitelyNotRight", true), "PlaceStartingCard doesn't throw IllegalStateException with wrong nickname and true");
        assertThrows(IllegalStateException.class, () -> setupState.placeStartingCard("definitelyNotRight", false), "PlaceStartingCard doesn't throw IllegalStateException with wrong nickname and false");
        try {
            assertEquals(board.getGamePhase(), GamePhase.PSCP);
            placeStartingCardUntilAll(4);
        }catch (IllegalStateException e){fail("PlaceStartingCard throws IllegalStareException even though there's no error");}
        catch (IllegalArgumentException e){fail("PlaceStartingCard throws IllegalArgumentException even though there's no error");}
        assertEquals(SetupState.class, setupState.getClass());
        assertEquals(SetupState.class, setupState.getClass());
        assertThrows(IllegalStateException.class, ()->setupState.placeStartingCard(board.getPlayerAreas().keySet().stream().findAny().map(Player::getNickname).toString(), true));
        assertEquals(board.getGamePhase(), GamePhase.CHOOSECOLOR);
    }
    public void placeStartingCardUntilAll(int numOfPlayers){
        setUp(numOfPlayers);
        assertEquals(SetupState.class, setupState.getClass());
        GameState nextGS=null;
        for(Player p : board.getPlayersByTurn()) {
            assertEquals(board.getGamePhase(), GamePhase.PSCP);
            setupState.placeStartingCard(p.getNickname(), true);
        }
        assertEquals(board.getGamePhase(), GamePhase.CHOOSECOLOR);
    }

    @Test
    public void chooseYourColorTest() {
        setUp(4);
        assertThrows(IllegalStateException.class, () -> setupState.chooseYourColor("definitelyNotRight", PlayerColor.BLUE),"ChooseYourColor doesn't throw IllegalStateException with wrong nickname");
        assertThrows(IllegalStateException.class, () -> setupState.chooseYourColor("definitelyNotRight", PlayerColor.RED),"ChooseYourColor doesn't throw IllegalStateException with wrong nickname");
        assertThrows(IllegalStateException.class, () -> setupState.chooseYourColor("definitelyNotRight", PlayerColor.GREEN),"ChooseYourColor doesn't throw IllegalStateException with wrong nickname");
        assertThrows(IllegalStateException.class, () -> setupState.chooseYourColor("definitelyNotRight", PlayerColor.YELLOW),"ChooseYourColor doesn't throw IllegalStateException with wrong nickname");
        assertThrows(IllegalStateException.class, () -> setupState.chooseYourColor(firstPlayer.getNickname(), PlayerColor.BLUE),"ChooseYourColor doesn't throw IllegalStateException with color==BLUE");
        assertThrows(IllegalStateException.class, () -> setupState.chooseYourColor(firstPlayer.getNickname(), PlayerColor.RED),"ChooseYourColor doesn't throw IllegalStateException with color==RED");
        assertThrows(IllegalStateException.class, () -> setupState.chooseYourColor(firstPlayer.getNickname(), PlayerColor.GREEN),"ChooseYourColor doesn't throw IllegalStateException with color==GREEN");
        assertThrows(IllegalStateException.class, () -> setupState.chooseYourColor(firstPlayer.getNickname(), PlayerColor.YELLOW),"ChooseYourColor doesn't throw IllegalStateException with color==YELLOW");
    }

    @ParameterizedTest
    @ValueSource (ints={-3,-2,-1,0,1,2,3,4,5})
    public void chooseSecretObjectiveTest(int i) {
        if(i==0 || i==1)
            assertThrows(IllegalStateException.class, () -> setupState.chooseSecretObjective("definitelyNotRight", i), "ChooseSecretObjective doesn't throw IllegalStateException with wrong nickname");
        else assertThrows(IllegalStateException.class, () -> setupState.chooseSecretObjective(firstPlayer.getNickname(), i), "ChooseSecretObjective doesn't throw IllegalStateException with choice=="+i);
    }

    @Test
    public void placeCardTest() {
        assertThrows(IllegalStateException.class, () -> setupState.placeCard("definitelyNotRight", "r0", new Point(0, 0), CornerDirection.TR, true), "PlaceCard doesn't throw IllegalStateException with wrong nickname");
        assertThrows(IllegalStateException.class, () -> setupState.placeCard(firstPlayer.getNickname(), "r0", new Point(0, 0), CornerDirection.TR, true),"PlaceCard doesn't throw IllegalStateException");
        assertThrows(IllegalStateException.class, () -> setupState.placeCard(firstPlayer.getNickname(), "r2", new Point(1, 0), CornerDirection.TL, false),"PlaceCard doesn't throw IllegalStateException");
        assertThrows(IllegalStateException.class, () -> setupState.placeCard(firstPlayer.getNickname(), "g22", new Point(0, 1), CornerDirection.BL, true),"PlaceCard doesn't throw IllegalStateException");
        assertThrows(IllegalStateException.class, () -> setupState.placeCard(firstPlayer.getNickname(), "g8", new Point(1, 1), CornerDirection.BL, false),"PlaceCard doesn't throw IllegalStateException");
    }

    @Test
    public void drawTest() {
        assertThrows(IllegalStateException.class, () -> setupState.draw("definitelyNotRight", 'r', 0), "Draw doesn't throw IllegalStateException with wrong nickname");
        assertThrows(IllegalStateException.class, () -> setupState.draw(firstPlayer.getNickname(), 'r', 0), "Draw doesn't throw IllegalStateException with deckFrom==r and cardPos==0");
        assertThrows(IllegalStateException.class, () -> setupState.draw(firstPlayer.getNickname(), 'r', 1), "Draw doesn't throw IllegalStateException with deckFrom==r and cardPos==1");
        assertThrows(IllegalStateException.class, () -> setupState.draw(firstPlayer.getNickname(), 'r', 2), "Draw doesn't throw IllegalStateException with deckFrom==r and cardPos==2");
        assertThrows(IllegalStateException.class, () -> setupState.draw(firstPlayer.getNickname(), 'g', 0), "Draw doesn't throw IllegalStateException with deckFrom==g and cardPos==0");
        assertThrows(IllegalStateException.class, () -> setupState.draw(firstPlayer.getNickname(), 'g', 1), "Draw doesn't throw IllegalStateException with deckFrom==g and cardPos==1");
        assertThrows(IllegalStateException.class, () -> setupState.draw(firstPlayer.getNickname(), 'g', 2),"Draw doesn't throw IllegalStateException with deckFrom==g and cardPos==2");
    }

    @Test
    public void startGameTest() {
        assertThrows(IllegalStateException.class, () -> setupState.startGame("definitelyNotRight", 2), "StartGame doesn't throw IllegalStateException with wrong nickname");
        assertThrows(IllegalStateException.class, () -> setupState.startGame(firstPlayer.getNickname(), 2), "StartGame doesn't throw IllegalStateException with numOfPlayers==2");
        assertThrows(IllegalStateException.class, () -> setupState.startGame(firstPlayer.getNickname(), 3),"StartGame doesn't throw IllegalStateException with numOfPlayers==3");
        assertThrows(IllegalStateException.class, () -> setupState.startGame(firstPlayer.getNickname(), 4),"StartGame doesn't throw IllegalStateException with numOfPlayers==4");
    }
}
