package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.ResourceCard;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.GameResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTurnSkipTest extends BaseBoardTest {
    @BeforeEach
    public void initBoard(){
        joinPlayers(2);
        board.getPlayerAreas().get(players[0]).placeStartingCard(new StartingCard());
        board.getPlayerAreas().get(players[1]).placeStartingCard(new StartingCard());
    }
    private void deadlockPlayer(Player p){
        PlayArea playArea = board.getPlayerAreas().get(p);
        assertNotNull(playArea);

        Runnable placeCard = () -> {
            ResourceCard blockedCard = new ResourceCard(GameResource.LEAF); // all corners FILLED on front
            blockedCard.turnFaceUp();
            playArea.placeCard(blockedCard, playArea.getFreeCorners().get(0));
        };
        while(!playArea.getFreeCorners().isEmpty()){
            placeCard.run();
        }
    }
    @Test
    public void playerDeadlockTest(){
        // assert correct identification of deadlocks
        // assert correct skipping of deadlocked player's turn
        // assert correct return value for the game continuing and the game ending due to deadlocks
        deadlockPlayer(players[0]);

        assertEquals(1, board.getCurrentTurn());
        assertTrue(board.nextTurn()); // player2 is not deadlocked, so game can continue
        assertFalse(board.getPlayerDeadlocks().get(players[1]));
        assertEquals(2, board.getCurrentTurn()); // player2 can play

        assertTrue(board.nextTurn()); // player2 is still not deadlocked, so game can continue
        assertTrue(board.getPlayerDeadlocks().get(players[0]));  // after nextTurn() skipping player1 the deadlock status should displayMessage
        assertFalse(board.getPlayerDeadlocks().get(players[1])); // player2 not deadlocked
        assertEquals(2, board.getCurrentTurn()); // player1 is deadlocked so his turn is skipped

        deadlockPlayer(players[1]);
        assertFalse(board.nextTurn());
    }
    @Test
    public void skipDisconnectedPlayersTest(){
        players[0].setConnected(false);
        assertEquals(1, board.getCurrentTurn());

        assertTrue(board.nextTurn()); // player2 is connected, so game can continue
        assertEquals(2, board.getCurrentTurn()); // player2 can play

        assertFalse(players[0].isConnected());
        assertTrue(board.nextTurn()); // player2 is still connected, so game can continue
        assertTrue(players[1].isConnected()); // player2 not disconnected
        assertEquals(2, board.getCurrentTurn()); // player1 is disconnected so his turn is skipped

        players[1].setConnected(false);
        assertFalse(board.nextTurn()); // all players disconnected
    }
    @Test
    public void skipMixedDisconnectedDeadlockedTest(){
        players[0].setConnected(false);
        deadlockPlayer(players[1]);

        assertFalse(board.nextTurn());
    }
}
