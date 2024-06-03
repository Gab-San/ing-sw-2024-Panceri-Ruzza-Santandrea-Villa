package it.polimi.ingsw.controller;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.stub.PuppetClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class JoinStateTest {
    private BoardController controller;
    private Board board;
    private final String playerNickname = "Flavio";

    private void setUp(int numOfPlayers){
        controller = new BoardController();
        controller.join(playerNickname, new PuppetClient());
        controller.setNumOfPlayers(playerNickname, numOfPlayers);
        board = controller.getGameState().board;
    }

    @ParameterizedTest
    @ValueSource(ints={2,3,4})
    public void joinUntilNextStateTest(int numOfPlayers){
        joinUntilSetupState(numOfPlayers);
    }

    private void joinUntilSetupState(int numOfPlayers){
        setUp(numOfPlayers);
        assertEquals(JoinState.class, controller.getGameState().getClass());
        GameState nextGS=null;
        for(int j = 2; j <= numOfPlayers; j++){
            controller.join("Player " + j, new PuppetClient());
            nextGS = controller.getGameState();
            if(j < numOfPlayers){
                assertNotNull(nextGS);
                assertEquals(JoinState.class, nextGS.getClass());
            }
        }
        assertNotNull(nextGS);
        assertEquals(SetupState.class, nextGS.getClass());
        assertEquals(board.getGamePhase(), GamePhase.PLACESTARTING);
    }


    @ParameterizedTest
    @ValueSource(ints={0,1,2,3,4,5,6})
    public void setNumOfPlayersTest(int i) {
        setUp(4);
        assertThrows(IllegalStateException.class, () -> controller.setNumOfPlayers("definitelyNotRight", i), "SetNumOfPlayers doesn't throw IllegalStateException wrong nickname");
        assertThrows(IllegalStateException.class, () -> controller.setNumOfPlayers(playerNickname, i), "SetNumOfPlayers doesn't throw IllegalStateException with i="+i);
    }

    @Test
    public void placeStartingCardTest() {
        setUp(4);
        assertThrows(IllegalStateException.class, () -> controller.placeStartingCard("definitelyNotRight", true), "PlaceStartingCard doesn't throw IllegalStateException with wrong nickname");
        assertThrows(IllegalStateException.class, () -> controller.placeStartingCard(playerNickname, true), "PlaceStartingCard doesn't throw IllegalStateException with placeOnFront==true");
        assertThrows(IllegalStateException.class, () -> controller.placeStartingCard(playerNickname, false), "PlaceStartingCard doesn't throw IllegalStateException with placeOnFront==false");
    }

    @Test
    public void chooseYourColorTest() {
        setUp(4);
        assertThrows(IllegalStateException.class, () -> controller.chooseYourColor("definitelyNotRight", PlayerColor.BLUE),"ChooseYourColor doesn't throw IllegalStateException with wrong nickname");
        assertThrows(IllegalStateException.class, () -> controller.chooseYourColor(playerNickname, PlayerColor.BLUE),"ChooseYourColor doesn't throw IllegalStateException with color==BLUE");
        assertThrows(IllegalStateException.class, () -> controller.chooseYourColor(playerNickname, PlayerColor.RED),"ChooseYourColor doesn't throw IllegalStateException with color==RED");
        assertThrows(IllegalStateException.class, () -> controller.chooseYourColor(playerNickname, PlayerColor.GREEN),"ChooseYourColor doesn't throw IllegalStateException with color==GREEN");
        assertThrows(IllegalStateException.class, () -> controller.chooseYourColor(playerNickname, PlayerColor.YELLOW),"ChooseYourColor doesn't throw IllegalStateException with color==YELLOW");
    }

    @Test
    public void chooseSecretObjectiveTest() {
        setUp(4);
        assertThrows(IllegalStateException.class, () -> controller.chooseSecretObjective("definitelyNotRight", 0), "ChooseSecretObjective doesn't throw IllegalStateException with wrong nickname");
        assertThrows(IllegalStateException.class, () -> controller.chooseSecretObjective(playerNickname, 0), "ChooseSecretObjective doesn't throw IllegalStateException with choice==0");
        assertThrows(IllegalStateException.class, () -> controller.chooseSecretObjective(playerNickname, 1), "ChooseSecretObjective doesn't throw IllegalStateException with choice==1");
    }

    @Test
    public void placeCardTest() {
        setUp(4);
        assertThrows(IllegalStateException.class, () -> controller.placeCard("definitelyNotRight", "r0", new Point(0, 0), CornerDirection.TR, true), "PlaceCard doesn't throw IllegalStateException with wrong nickname");
        assertThrows(IllegalStateException.class, () -> controller.placeCard(playerNickname, "r0", new Point(0, 0), CornerDirection.TR, true),"PlaceCard doesn't throw IllegalStateException");
        assertThrows(IllegalStateException.class, () -> controller.placeCard(playerNickname, "r2", new Point(1, 0), CornerDirection.TL, false),"PlaceCard doesn't throw IllegalStateException");
        assertThrows(IllegalStateException.class, () -> controller.placeCard(playerNickname, "g22", new Point(0, 1), CornerDirection.BL, true),"PlaceCard doesn't throw IllegalStateException");
        assertThrows(IllegalStateException.class, () -> controller.placeCard(playerNickname, "g8", new Point(1, 1), CornerDirection.BL, false),"PlaceCard doesn't throw IllegalStateException");
    }

    @Test
    public void drawTest() {
        setUp(4);
        assertThrows(IllegalStateException.class, () -> controller.draw("definitelyNotRight", 'r', 0), "Draw doesn't throw IllegalStateException with wrong nickname");
        assertThrows(IllegalStateException.class, () -> controller.draw(playerNickname, 'r', 0), "Draw doesn't throw IllegalStateException with deckFrom==r and cardPos==0");
        assertThrows(IllegalStateException.class, () -> controller.draw(playerNickname, 'r', 1), "Draw doesn't throw IllegalStateException with deckFrom==r and cardPos==1");
        assertThrows(IllegalStateException.class, () -> controller.draw(playerNickname, 'r', 2), "Draw doesn't throw IllegalStateException with deckFrom==r and cardPos==2");
        assertThrows(IllegalStateException.class, () -> controller.draw(playerNickname, 'g', 0), "Draw doesn't throw IllegalStateException with deckFrom==g and cardPos==0");
        assertThrows(IllegalStateException.class, () -> controller.draw(playerNickname, 'g', 1), "Draw doesn't throw IllegalStateException with deckFrom==g and cardPos==1");
        assertThrows(IllegalStateException.class, () -> controller.draw(playerNickname, 'g', 2),"Draw doesn't throw IllegalStateException with deckFrom==g and cardPos==2");
    }

    @Test
    public void startGameTest() {
        setUp(4);
        assertThrows(IllegalStateException.class, () -> controller.restartGame("definitelyNotRight", 2), "StartGame doesn't throw IllegalStateException with wrong nickname");
        assertThrows(IllegalStateException.class, () -> controller.restartGame(playerNickname, 2), "StartGame doesn't throw IllegalStateException with numOfPlayers==2");
        assertThrows(IllegalStateException.class, () -> controller.restartGame(playerNickname, 3),"StartGame doesn't throw IllegalStateException with numOfPlayers==3");
        assertThrows(IllegalStateException.class, () -> controller.restartGame(playerNickname, 4),"StartGame doesn't throw IllegalStateException with numOfPlayers==4");
    }

    @ParameterizedTest
    @ValueSource (ints = {2,3,4})
    public void joinAndDisconnectTest(int numOfPlayers) {
        setUp(numOfPlayers);
        int numConnPlayers=board.getPlayerAreas().size();
        int i=0;
        System.out.println("numConnPlayers is "+ numConnPlayers);
        System.out.println(board.getPlayerAreas().keySet());

        while(numConnPlayers<numOfPlayers && numConnPlayers>0){

            if(new Random().nextBoolean())
                controller.join("Player"+i, new PuppetClient());
            else{
                Player player = board.getPlayerAreas().keySet().stream().findAny().orElse(null);
                assert player != null;
                controller.disconnect(player.getNickname());
            }

            numConnPlayers=board.getPlayerAreas().size();
            i++;
            System.out.println("numConnPlayers is "+ numConnPlayers);
        }

        if(numConnPlayers==0){
            board = controller.getGameState().board; // board changed on return to creation phase
            assertEquals(GamePhase.CREATE, board.getGamePhase(), "wrong phase: \""+ board.getGamePhase() + "\" instead of \""+ GamePhase.CREATE +"\".");
            assertEquals(CreationState.class, controller.getGameState().getClass(), "Wrong state: "+ controller.getGameState().getClass() + "instead of "+ CreationStateTest.class +".");
            assertTrue(board.getPlayerAreas().isEmpty(), "PlayerAreas ain't empty");
        }
        else{
            assertEquals(numConnPlayers, numOfPlayers, "ERRORE SOMEWHERE");
            assertEquals(GamePhase.PLACESTARTING, board.getGamePhase(), "wrong phase: \""+ board.getGamePhase() + "\" instead of \""+ GamePhase.PLACESTARTING +"\".");
            assertEquals(SetupState.class, controller.getGameState().getClass(), "Wrong state: "+ controller.getGameState().getClass() + "instead of "+ SetupState.class +".");
            assertEquals(board.getPlayerAreas().size(), numOfPlayers, "PlayerAreas ain't empty");
        }
    }
}
