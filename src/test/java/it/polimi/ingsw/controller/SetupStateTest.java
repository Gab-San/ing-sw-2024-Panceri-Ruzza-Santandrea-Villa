package it.polimi.ingsw.controller;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.controller.testclass.PuppetClient;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.PlayerColor;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

//TODO QUESTO TEST VA COMPLETATO!!!

public class SetupStateTest {
    private BoardController controller;
    private Board board;
    private final String playerNickname = "Flavio";

    public void setUp(int numOfPlayers) {
        controller = new BoardController("Flavio's Game");
        controller.join(playerNickname, new PuppetClient());
        controller.setNumOfPlayers(playerNickname, numOfPlayers);
        board = controller.getGameState().board;
        joinUntilSetupState(numOfPlayers);
    }


    public void joinUntilSetupState(int numOfPlayers) {
        for (int j = 2; j <= numOfPlayers; j++) {
            controller.join("Player " + j, new PuppetClient());
        }
    }

    public void advanceToPlayState(int numOfPlayers){
        setUp(numOfPlayers);
        placeAllStarting(numOfPlayers);
        giveAllColors(numOfPlayers);
        giveAllSecretObjectives(numOfPlayers);

        assertEquals(PlayState.class, controller.getGameState().getClass());
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3, 4})
    public void joinTest(int numOfPlayers) {
        setUp(numOfPlayers);
        for(int i=0; i<3;i++) {
            if(i==1)
                placeAllStarting(numOfPlayers);
            if(i==2)
                giveAllColors(numOfPlayers);
            assertThrows(IllegalStateException.class, () -> controller.join(playerNickname, new PuppetClient()), "Join doesn't throw IllegalStateException with client!=null");
        }
    }

    @ParameterizedTest
    @ValueSource(ints={2,3,4})
    public void setNumOfPlayersTest(int numOfPlayers) {
        setUp(numOfPlayers);
        for(int i=0; i<3;i++) {
            if (i == 1)
                placeAllStarting(numOfPlayers);
            if (i == 2)
                giveAllColors(numOfPlayers);
            assertThrows(IllegalStateException.class, () -> controller.setNumOfPlayers("definitelyNotRight", new Random().nextInt(3)+2), "SetNumOfPlayers doesn't throw IllegalStateException wrong nickname");
            assertThrows(IllegalStateException.class, () -> controller.setNumOfPlayers(playerNickname, new Random().nextInt(3)+2), "SetNumOfPlayers doesn't throw IllegalStateException with i=" + i);
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3, 4})
    public void placeStartingCardTest(int numOfPlayers) {
        setUp(numOfPlayers);
        assertEquals(board.getGamePhase(), GamePhase.PLACESTARTING);
        assertEquals(controller.getGameState().getClass(), SetupState.class);

        assertThrows(IllegalArgumentException.class, () -> controller.placeStartingCard("definitelyNotRight", true), "PlaceStartingCard doesn't throw IllegalStateException with wrong nickname and true");
        assertThrows(IllegalArgumentException.class, () -> controller.placeStartingCard("definitelyNotRight", false), "PlaceStartingCard doesn't throw IllegalStateException with wrong nickname and false");

        try {
            assertEquals(board.getGamePhase(), GamePhase.PLACESTARTING);
            assertEquals(controller.getGameState().getClass(), SetupState.class);
            placeStartingCardUntilAll(numOfPlayers);
        } catch (IllegalStateException e) {
            fail("PlaceStartingCard throws IllegalStareException even though there's no error");
        } catch (IllegalArgumentException e) {
            fail("PlaceStartingCard throws IllegalArgumentException even though there's no error");
        }

        assertEquals(controller.getGameState().getClass(), SetupState.class);
        assertEquals(board, controller.getGameState().board);
        assertEquals(board.getGamePhase(), GamePhase.CHOOSECOLOR);

        //controllo che dopo la fine mi throwa le eccezioni necessarie se richiamo lo stesso metodo anche correttamente
        assertThrows(IllegalStateException.class, () -> controller.placeStartingCard(board.getPlayerAreas().keySet().stream().findAny().map(Player::getNickname).toString(), new Random().nextBoolean()));
    }

    private void placeStartingCardUntilAll(int numOfPlayers) {
        //GameState nextGS=null; inutile, metodo void
        for (Player p : board.getPlayerAreas().keySet()) {
            assertEquals(controller.getGameState().board.getGamePhase(), GamePhase.PLACESTARTING);
            assertEquals(controller.getGameState().getClass(), SetupState.class);
            controller.placeStartingCard(p.getNickname(), new Random().nextBoolean());
        }
        assertEquals(controller.getGameState().board.getGamePhase(), GamePhase.CHOOSECOLOR);
        assertEquals(controller.getGameState().getClass(), SetupState.class);
    }

    private void placeAllStarting(int numOfPlayers) {
        for (Player p : board.getPlayerAreas().keySet()) {
            controller.placeStartingCard(p.getNickname(), new Random().nextBoolean());
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3, 4})
    public void chooseYourColorTest(int numOfPlayers) {
        setUp(numOfPlayers);
        placeAllStarting(numOfPlayers);
        assertEquals(board.getGamePhase(), GamePhase.CHOOSECOLOR);
        assertEquals(controller.getGameState().getClass(), SetupState.class);

        assertThrows(IllegalArgumentException.class, () -> controller.chooseYourColor("definitelyNotRight", PlayerColor.RED), "PlaceStartingCard doesn't throw IllegalStateException with wrong nickname and true");
        assertThrows(IllegalArgumentException.class, () -> controller.chooseYourColor("definitelyNotRight", PlayerColor.BLUE), "PlaceStartingCard doesn't throw IllegalStateException with wrong nickname and true");
        assertThrows(IllegalArgumentException.class, () -> controller.chooseYourColor("definitelyNotRight", PlayerColor.YELLOW), "PlaceStartingCard doesn't throw IllegalStateException with wrong nickname and true");
        assertThrows(IllegalArgumentException.class, () -> controller.chooseYourColor("definitelyNotRight", PlayerColor.GREEN), "PlaceStartingCard doesn't throw IllegalStateException with wrong nickname and true");

        try {
            assertEquals(board.getGamePhase(), GamePhase.CHOOSECOLOR);
            assertEquals(controller.getGameState().getClass(), SetupState.class);
            chooseYourColorForAll(numOfPlayers);
        } catch (IllegalStateException e) {
            fail("PlaceStartingCard throws IllegalStareException even though there's no error");
        } catch (IllegalArgumentException e) {
            fail("PlaceStartingCard throws IllegalArgumentException even though there's no error");
        }

        assertEquals(controller.getGameState().getClass(), SetupState.class);
        assertEquals(board, controller.getGameState().board);
        assertEquals(board.getGamePhase(), GamePhase.CHOOSEOBJECTIVE);

        //controllo che dopo la fine mi throwa le eccezioni necessarie se richiamo lo stesso metodo anche correttamente
        assertThrows(IllegalStateException.class, () -> controller.chooseYourColor(board.getPlayerAreas().keySet().stream().findAny().map(Player::getNickname).toString(), PlayerColor.RED), "PlaceStartingCard doesn't throw IllegalStateException with wrong nickname and true");
        assertThrows(IllegalStateException.class, () -> controller.chooseYourColor(board.getPlayerAreas().keySet().stream().findAny().map(Player::getNickname).toString(), PlayerColor.BLUE), "PlaceStartingCard doesn't throw IllegalStateException with wrong nickname and true");
        assertThrows(IllegalStateException.class, () -> controller.chooseYourColor(board.getPlayerAreas().keySet().stream().findAny().map(Player::getNickname).toString(), PlayerColor.YELLOW), "PlaceStartingCard doesn't throw IllegalStateException with wrong nickname and true");
        assertThrows(IllegalStateException.class, () -> controller.chooseYourColor(board.getPlayerAreas().keySet().stream().findAny().map(Player::getNickname).toString(), PlayerColor.GREEN), "PlaceStartingCard doesn't throw IllegalStateException with wrong nickname and true");
    }

    private void chooseYourColorForAll(int numOfPlayers) {
        for (Player p : board.getPlayerAreas().keySet()) {
            assertEquals(controller.getGameState().board.getGamePhase(), GamePhase.CHOOSECOLOR);
            assertEquals(controller.getGameState().getClass(), SetupState.class);
            controller.chooseYourColor(p.getNickname(), board.getRandomAvailableColor());
        }
        assertEquals(controller.getGameState().board.getGamePhase(), GamePhase.CHOOSEOBJECTIVE);
        assertEquals(controller.getGameState().getClass(), SetupState.class);
    }

    private void giveAllColors(int numOfPlayers) {
        for (Player p : board.getPlayerAreas().keySet()) {
            controller.chooseYourColor(p.getNickname(), board.getRandomAvailableColor());
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3, 4})
    public void chooseSecretObjectiveTest(int numOfPlayers) {
        setUp(numOfPlayers);
        placeAllStarting(numOfPlayers);
        giveAllColors(numOfPlayers);
        assertEquals(board.getGamePhase(), GamePhase.CHOOSEOBJECTIVE);
        assertEquals(controller.getGameState().getClass(), SetupState.class);

        assertThrows(IllegalArgumentException.class, () -> controller.chooseSecretObjective("definitelyNotRight", 1), "PlaceStartingCard doesn't throw IllegalStateException with wrong nickname and true");
        assertThrows(IllegalArgumentException.class, () -> controller.chooseSecretObjective("definitelyNotRight", 2), "PlaceStartingCard doesn't throw IllegalStateException with wrong nickname and true");

        try {
            assertEquals(board.getGamePhase(), GamePhase.CHOOSEOBJECTIVE);
            assertEquals(controller.getGameState().getClass(), SetupState.class);
            chooseSecretObjectiveForAll(numOfPlayers);
        } catch (IllegalStateException e) {
            fail("PlaceStartingCard throws IllegalStareException even though there's no error");
        } catch (IllegalArgumentException e) {
            fail("PlaceStartingCard throws IllegalArgumentException even though there's no error");
        }

        assertEquals(controller.getGameState().getClass(), PlayState.class);
        assertEquals(board, controller.getGameState().board);
        assertEquals(board.getGamePhase(), GamePhase.PLACECARD);

        //controllo che dopo la fine mi throwa le eccezioni necessarie se richiamo lo stesso metodo anche correttamente
        assertThrows(IllegalStateException.class, () -> controller.chooseSecretObjective(board.getPlayerAreas().keySet().stream().findAny().map(Player::getNickname).toString(), new Random().nextInt(2)+1), "PlaceStartingCard doesn't throw IllegalStateException with wrong nickname and true");

    }

    private void chooseSecretObjectiveForAll(int numOfPlayers){
        for (Player p : board.getPlayerAreas().keySet()) {
            assertEquals(controller.getGameState().board.getGamePhase(), GamePhase.CHOOSEOBJECTIVE);
            assertEquals(controller.getGameState().getClass(), SetupState.class);
            controller.chooseSecretObjective(p.getNickname(), new Random().nextInt(2)+1);
        }
        assertEquals(controller.getGameState().board.getGamePhase(), GamePhase.PLACECARD);
        assertEquals(controller.getGameState().getClass(), PlayState.class);
    }

    private void giveAllSecretObjectives(int numOpPlayers){
        for(Player p : board.getPlayerAreas().keySet()){
            controller.chooseSecretObjective(p.getNickname(), new Random().nextInt(2)+1);
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3, 4})
    public void placeCardTest(int numOfPlayers) {
        setUp(numOfPlayers);
        for(int i=0; i<3;i++) {
            if (i == 1)
                placeAllStarting(numOfPlayers);
            if (i == 2)
                giveAllColors(numOfPlayers);
            assertThrows(IllegalStateException.class, () -> controller.placeCard("definitelyNotRight", "r0", new Point(0, 0), CornerDirection.TR, true), "PlaceCard doesn't throw IllegalStateException with wrong nickname");
            assertThrows(IllegalStateException.class, () -> controller.placeCard(playerNickname, "r0", new Point(0, 0), CornerDirection.TR, true), "PlaceCard doesn't throw IllegalStateException");
            assertThrows(IllegalStateException.class, () -> controller.placeCard(playerNickname, "r2", new Point(1, 0), CornerDirection.TL, false), "PlaceCard doesn't throw IllegalStateException");
            assertThrows(IllegalStateException.class, () -> controller.placeCard(playerNickname, "g22", new Point(0, 1), CornerDirection.BL, true), "PlaceCard doesn't throw IllegalStateException");
            assertThrows(IllegalStateException.class, () -> controller.placeCard(playerNickname, "g8", new Point(1, 1), CornerDirection.BL, false), "PlaceCard doesn't throw IllegalStateException");
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3, 4})
    public void drawTest(int numOfPlayers) {
        setUp(numOfPlayers);
        for(int i=0; i<3;i++) {
            if(i==1)
                placeAllStarting(numOfPlayers);
            if(i==2)
                giveAllColors(numOfPlayers);
            assertThrows(IllegalStateException.class, () -> controller.draw("definitelyNotRight", 'r', 0), "Draw doesn't throw IllegalStateException with wrong nickname");
            assertThrows(IllegalStateException.class, () -> controller.draw(playerNickname, 'r', 0), "Draw doesn't throw IllegalStateException with deckFrom==r and cardPos==0");
            assertThrows(IllegalStateException.class, () -> controller.draw(playerNickname, 'r', 1), "Draw doesn't throw IllegalStateException with deckFrom==r and cardPos==1");
            assertThrows(IllegalStateException.class, () -> controller.draw(playerNickname, 'r', 2), "Draw doesn't throw IllegalStateException with deckFrom==r and cardPos==2");
            assertThrows(IllegalStateException.class, () -> controller.draw(playerNickname, 'g', 0), "Draw doesn't throw IllegalStateException with deckFrom==g and cardPos==0");
            assertThrows(IllegalStateException.class, () -> controller.draw(playerNickname, 'g', 1), "Draw doesn't throw IllegalStateException with deckFrom==g and cardPos==1");
            assertThrows(IllegalStateException.class, () -> controller.draw(playerNickname, 'g', 2), "Draw doesn't throw IllegalStateException with deckFrom==g and cardPos==2");
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3, 4})
    public void startGameTest(int numOfPlayers) {
        setUp(numOfPlayers);
        for(int i=0; i<3;i++) {
            if (i == 1)
                placeAllStarting(numOfPlayers);
            if (i == 2)
                giveAllColors(numOfPlayers);

            assertThrows(IllegalStateException.class, () -> controller.restartGame("definitelyNotRight", 2), "StartGame doesn't throw IllegalStateException with wrong nickname");
            assertThrows(IllegalStateException.class, () -> controller.restartGame(playerNickname, 2), "StartGame doesn't throw IllegalStateException with numOfPlayers==2");
            assertThrows(IllegalStateException.class, () -> controller.restartGame(playerNickname, 3), "StartGame doesn't throw IllegalStateException with numOfPlayers==3");
            assertThrows(IllegalStateException.class, () -> controller.restartGame(playerNickname, 4), "StartGame doesn't throw IllegalStateException with numOfPlayers==4");
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3, 4})
    public void testWithDisconnect(int numOfPlayers){
        setUp(numOfPlayers);
        for(Player p: board.getPlayerAreas().keySet())
            controller.disconnect(p.getNickname());
        assertEquals(GamePhase.PLACECARD, board.getGamePhase());
        assertEquals(PlayState.class ,controller.getGameState().getClass());
    }
}
