package it.polimi.ingsw.controller;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.controller.testclass.PuppetClient;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.PlayerColor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

//TODO QUESTO TEST VA COMPLETATO!!!

public class SetupStateTest extends GeneralControlTest{
    private BoardController controller;
    private Board board;
    private final String playerNickname = "Flavio";

    public void setUp(int numOfPlayers){
        controller = new BoardController("Flavio's Game");
        controller.join(playerNickname, new PuppetClient());
        controller.setNumOfPlayers(playerNickname, numOfPlayers);
        board = controller.getGameState().board;
        joinUntilSetupState(numOfPlayers);
    }


      public void joinUntilSetupState(int numOfPlayers){
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

//    public void advanceToPlayState(int numOfPlayers){
//        setUp(numOfPlayers);
//        //FIXME i giocatori non hanno ancora un turno associato
//              [Ale] non serve, la chiamata è per avere una lista di player, non importa l'ordine
//        List<Player> players = board.getPlayersByTurn();
//        for(Player p : players){
//            controller.placeStartingCard(p.getNickname(), false);
//        }
//        for(Player p : players){
//            assertDoesNotThrow(()->controller.chooseYourColor(p.getNickname(), board.getRandomAvailableColor()));
//        }
//        GameState nextState = null;
//        for(Player p : players){
//            nextState = controller.chooseSecretObjective(p.getNickname(), 1);
//        }
//        assertNotNull(nextState);
//        assertEquals(PlayState.class, nextState.getClass());
//        return nextState;
//    }
//
//    @Test
//    public void joinTest() {
//        setUp(4);
//        assertThrows(IllegalStateException.class, () -> controller.join(playerNickname, null), "Join doesn't throw IllegalStateException with client==null");
//    }
//
//    @ParameterizedTest
//    @ValueSource(ints={0,1,2,3,4,5,6})
//    public void setNumOfPlayersTest(int i) {
//        setUp(4);
//        assertThrows(IllegalStateException.class, () -> controller.setNumOfPlayers("definitelyNotRight", i), "SetNumOfPlayers doesn't throw IllegalStateException wrong nickname");
//        assertThrows(IllegalStateException.class, () -> controller.setNumOfPlayers(playerNickname, i), "SetNumOfPlayers doesn't throw IllegalStateException with i="+i);
//    }
//
    @ParameterizedTest
    @ValueSource(ints = {2,3,4})
    public void placeStartingCardTest(int numOfPlayers) {
        setUp(numOfPlayers);

        assertEquals(board.getGamePhase(), GamePhase.PLACESTARTING);
        assertEquals(controller.getGameState().getClass(), SetupState.class);

        //assertThrows(NullPointerException.class, );
        assertThrows(IllegalArgumentException.class, () -> controller.placeStartingCard("definitelyNotRight", true), "PlaceStartingCard doesn't throw IllegalStateException with wrong nickname and true");
        assertThrows(IllegalArgumentException.class, () -> controller.placeStartingCard("definitelyNotRight", false), "PlaceStartingCard doesn't throw IllegalStateException with wrong nickname and false");
        try {
            assertEquals(board.getGamePhase(), GamePhase.PLACESTARTING);
            assertEquals(controller.getGameState().getClass(), SetupState.class);
            placeStartingCardUntilAll(numOfPlayers);
        }catch (IllegalStateException e){fail("PlaceStartingCard throws IllegalStareException even though there's no error");}
        catch (IllegalArgumentException e){fail("PlaceStartingCard throws IllegalArgumentException even though there's no error");}
        assertEquals(controller.getGameState().getClass(), SetupState.class);
        assertEquals(board, controller.getGameState().board);
        assertEquals(board.getGamePhase(), GamePhase.CHOOSECOLOR);
        //controllo che dopo la fine mi throwa le eccezioni necessarie se richiamo lo stesso metodo anche correttamente
        assertThrows(IllegalStateException.class, ()->controller.placeStartingCard(board.getPlayerAreas().keySet().stream().findAny().map(Player::getNickname).toString(), true));
    }
    public void placeStartingCardUntilAll(int numOfPlayers){
        //setUp(numOfPlayers); già fatto nel metodo chiamante
        //assertEquals(board.getGamePhase(), GamePhase.PLACESTARTING); già controllato
        //GameState nextGS=null; inutile, metodo void
        for(Player p : board.getPlayersByTurn()) {
            assertEquals(controller.getGameState().board.getGamePhase(), GamePhase.PLACESTARTING);
            assertEquals(controller.getGameState().getClass(), SetupState.class);
            controller.placeStartingCard(p.getNickname(), true);
        }
        assertEquals(controller.getGameState().board.getGamePhase(), GamePhase.CHOOSECOLOR);
        assertEquals(controller.getGameState().getClass(), SetupState.class);
    }

    @ParameterizedTest
    @ValueSource (ints={2,3,4})
    public void chooseYourColorTest(int numOfPlayers) {
        setUp(numOfPlayers);
        placeStartingCardUntilAll(numOfPlayers);

        assertEquals(board.getGamePhase(), GamePhase.CHOOSECOLOR);
        assertEquals(controller.getGameState().getClass(), SetupState.class);

        for(Player p : board.getPlayerAreas().keySet())
            controller.chooseYourColor(p.getNickname(), board.getRandomAvailableColor());

        assertThrows(IllegalArgumentException.class,()-> controller.chooseSecretObjective("Player 1, ", 0), "error on chose");
        // assertThrows(IllegalArgumentException.class, () -> controller.chooseYourColor("definitelyNotRight", board.getRandomAvailableColor()),"ChooseYourColor doesn't throw IllegalStateException with wrong nickname");

    }

//    @ParameterizedTest
//    @ValueSource (ints={-3,-2,-1,0,1,2,3,4,5})
//    public void chooseSecretObjectiveTest(int i) {
//        if(i==0 || i==1)
//            assertThrows(IllegalStateException.class, () -> controller.chooseSecretObjective("definitelyNotRight", i), "ChooseSecretObjective doesn't throw IllegalStateException with wrong nickname");
//        else assertThrows(IllegalStateException.class, () -> controller.chooseSecretObjective(playerNickname, i), "ChooseSecretObjective doesn't throw IllegalStateException with choice=="+i);
//    }
//
//    @Test
//    public void placeCardTest() {
//        assertThrows(IllegalStateException.class, () -> controller.placeCard("definitelyNotRight", "r0", new Point(0, 0), CornerDirection.TR, true), "PlaceCard doesn't throw IllegalStateException with wrong nickname");
//        assertThrows(IllegalStateException.class, () -> controller.placeCard(playerNickname, "r0", new Point(0, 0), CornerDirection.TR, true),"PlaceCard doesn't throw IllegalStateException");
//        assertThrows(IllegalStateException.class, () -> controller.placeCard(playerNickname, "r2", new Point(1, 0), CornerDirection.TL, false),"PlaceCard doesn't throw IllegalStateException");
//        assertThrows(IllegalStateException.class, () -> controller.placeCard(playerNickname, "g22", new Point(0, 1), CornerDirection.BL, true),"PlaceCard doesn't throw IllegalStateException");
//        assertThrows(IllegalStateException.class, () -> controller.placeCard(playerNickname, "g8", new Point(1, 1), CornerDirection.BL, false),"PlaceCard doesn't throw IllegalStateException");
//    }
//
//    @Test
//    public void drawTest() {
//        assertThrows(IllegalStateException.class, () -> controller.draw("definitelyNotRight", 'r', 0), "Draw doesn't throw IllegalStateException with wrong nickname");
//        assertThrows(IllegalStateException.class, () -> controller.draw(playerNickname, 'r', 0), "Draw doesn't throw IllegalStateException with deckFrom==r and cardPos==0");
//        assertThrows(IllegalStateException.class, () -> controller.draw(playerNickname, 'r', 1), "Draw doesn't throw IllegalStateException with deckFrom==r and cardPos==1");
//        assertThrows(IllegalStateException.class, () -> controller.draw(playerNickname, 'r', 2), "Draw doesn't throw IllegalStateException with deckFrom==r and cardPos==2");
//        assertThrows(IllegalStateException.class, () -> controller.draw(playerNickname, 'g', 0), "Draw doesn't throw IllegalStateException with deckFrom==g and cardPos==0");
//        assertThrows(IllegalStateException.class, () -> controller.draw(playerNickname, 'g', 1), "Draw doesn't throw IllegalStateException with deckFrom==g and cardPos==1");
//        assertThrows(IllegalStateException.class, () -> controller.draw(playerNickname, 'g', 2),"Draw doesn't throw IllegalStateException with deckFrom==g and cardPos==2");
//    }
//
//    @Test
//    public void startGameTest() {
//        assertThrows(IllegalStateException.class, () -> controller.startGame("definitelyNotRight", 2), "StartGame doesn't throw IllegalStateException with wrong nickname");
//        assertThrows(IllegalStateException.class, () -> controller.startGame(playerNickname, 2), "StartGame doesn't throw IllegalStateException with numOfPlayers==2");
//        assertThrows(IllegalStateException.class, () -> controller.startGame(playerNickname, 3),"StartGame doesn't throw IllegalStateException with numOfPlayers==3");
//        assertThrows(IllegalStateException.class, () -> controller.startGame(playerNickname, 4),"StartGame doesn't throw IllegalStateException with numOfPlayers==4");
//    }
}
