package it.polimi.ingsw.controller;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.controller.testclass.PuppetClient;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.PlayerColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

//TODO QUESTO TEST VA COMPLETATO!

class EndgameStateTest {

    private BoardController controller;
    private Board board;
    private final String playerNickname = "Flavio";
    private final String secondPlNick = "Player 2";

    @BeforeEach
    public void setUp() {
        controller = new BoardController("Flavio's Game");
        controller.join(playerNickname, new PuppetClient());
        controller.setNumOfPlayers(playerNickname, 2);
        board = controller.getGameState().board;
        joinUntilPlayState(2);
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

    private void joinUntilPlayState(int numOfPlayers) {
        joinUntilSetupState(numOfPlayers);
        controller.placeStartingCard(playerNickname, false);
        controller.placeStartingCard(secondPlNick, false);
        controller.chooseYourColor(playerNickname, PlayerColor.BLUE);
        controller.chooseYourColor(secondPlNick, PlayerColor.GREEN);
        controller.chooseSecretObjective(playerNickname, 1);
        controller.chooseSecretObjective(secondPlNick, 2);
        Player currentPlayer = board.getCurrentPlayer();
        controller.placeCard(currentPlayer.getNickname(),
                currentPlayer.getHand().getCard(0).getCardID(),
                new Point(0,0),
                CornerDirection.TR,
                false
                );

        controller.draw(currentPlayer.getNickname(),
                'R',
                0
                );
        currentPlayer = board.getCurrentPlayer();
        controller.placeCard(currentPlayer.getNickname(),
                currentPlayer.getHand().getCard(0).getCardID(),
                new Point(0,0),
                CornerDirection.TR,
                false);
        controller.draw(currentPlayer.getNickname(),
                'R',
                0
        );
        currentPlayer = board.getCurrentPlayer();
        board.addScore(currentPlayer, 20);
        controller.placeCard(currentPlayer.getNickname(),
                currentPlayer.getHand().getCard(0).getCardID(),
                new Point(0,0),
                CornerDirection.TL,
                false
        );

        controller.draw(currentPlayer.getNickname(),
                'R',
                0
        );
        currentPlayer = board.getCurrentPlayer();
        controller.placeCard(currentPlayer.getNickname(),
                currentPlayer.getHand().getCard(0).getCardID(),
                new Point(0,0),
                CornerDirection.TL,
                false);
        controller.draw(currentPlayer.getNickname(),
                'R',
                0
        );
        currentPlayer = board.getCurrentPlayer();
        controller.placeCard(currentPlayer.getNickname(),
                currentPlayer.getHand().getCard(0).getCardID(),
                new Point(0,0),
                CornerDirection.BL,
                false);
        controller.draw(currentPlayer.getNickname(),
                'R',
                0
        );
        currentPlayer = board.getCurrentPlayer();
        controller.placeCard(currentPlayer.getNickname(),
                currentPlayer.getHand().getCard(0).getCardID(),
                new Point(0,0),
                CornerDirection.BL,
                false);
        controller.draw(currentPlayer.getNickname(),
                'R',
                0
        );

        assertEquals(EndgameState.class, controller.getGameState().getClass());
    }

    @Test
    void join() {
        assertThrows(
                IllegalStateException.class,
                () -> controller.join("Cugola", new PuppetClient())
        );

    }

    @Test
    void setNumOfPlayers() {
        assertThrows(
                IllegalStateException.class,
                () -> controller.setNumOfPlayers("Cugole", 3)
        );

    }

    @Test
    void disconnect() {
    }

    @Test
    void placeStartingCard() {
        assertThrows(
                IllegalStateException.class,
                () -> controller.placeStartingCard("Cugola", true)
        );
    }

    @Test
    void chooseYourColor() {
        assertThrows(
                IllegalStateException.class,
                () -> controller.chooseYourColor("Cannarsi", PlayerColor.GREEN)
        );
    }

    @Test
    void chooseSecretObjective() {
        assertThrows(
                IllegalStateException.class,
                () -> controller.chooseSecretObjective("Heidi", 2)
        );
    }

    @Test
    void draw() {
        assertThrows(
                IllegalStateException.class,
                () -> controller.draw("Fuffy", 'R', 2)
        );
    }

    @Test
    void placeCard() {
        assertThrows(
                IllegalStateException.class,
                () -> controller.placeCard("Donnina bella", "SUS", new Point(69,69),
                        CornerDirection.TR, true)
        );

    }

    @Test
    void startGame() {
    }
}