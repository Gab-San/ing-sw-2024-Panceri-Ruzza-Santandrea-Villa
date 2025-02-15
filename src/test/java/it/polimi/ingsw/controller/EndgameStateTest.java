package it.polimi.ingsw.controller;

import it.polimi.ingsw.*;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.stub.PuppetClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EndgameStateTest {

    private BoardController controller;
    private Board board;
    private final String playerNickname = "Flavio";
    private final String secondPlNick = "Player 2";

    @BeforeEach
    public void setUp() {
        init();
        joinUntilSetupState(3);
        playUntilPlayState();
        playUntilLastTurn();
        playLastTurn();
    }

    private void init(){
        controller = new BoardController();
        VirtualClient flaviosClient = new PuppetClient();
        controller.join(playerNickname, flaviosClient);
        controller.setNumOfPlayers(playerNickname, 3);
        board = controller.getGameState().board;
    }

    private void joinUntilSetupState(int numOfPlayers){
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

    private void playUntilPlayState() {
        controller.placeStartingCard(playerNickname, false);
        controller.placeStartingCard(secondPlNick, false);
        controller.placeStartingCard("Player 3", false);
        //CHOOSING COLOR
        //FOR TWO PLAYERS
        controller.chooseYourColor(playerNickname, PlayerColor.BLUE);
        controller.chooseYourColor(secondPlNick, PlayerColor.GREEN);
        //FOR THREE PLAYERS
        controller.chooseYourColor("Player 3", PlayerColor.YELLOW);
        //FOR FOUR PLAYERS
//        controller.chooseYourColor("Player 4", PlayerColor.RED);

        //CHOOSING SECRET OBJ
        //FOR TWO PLAYERS
        controller.chooseSecretObjective(playerNickname, 1);
        controller.chooseSecretObjective(secondPlNick, 2);
        //FOR THREE PLAYERS
        controller.chooseSecretObjective("Player 3", 2);
        //FOR FOUR PLAYERS
//        controller.chooseSecretObjective("Player 4", 1);
        assertEquals(PlayState.class, controller.getGameState().getClass());
    }

    private void playUntilLastTurn(){
        // FIRST TURN
        Player currentPlayer = board.getCurrentPlayer();
        controller.placeCard(currentPlayer.getNickname(),
                currentPlayer.getHand().getCard(0).getCardID(),
                new GamePoint(0,0),
                CornerDirection.TR,
                false
                );

        controller.draw(currentPlayer.getNickname(),
                'R',
                0
                );
        //SECOND TURN
        currentPlayer = board.getCurrentPlayer();
        controller.placeCard(currentPlayer.getNickname(),
                currentPlayer.getHand().getCard(0).getCardID(),
                new GamePoint(0,0),
                CornerDirection.TR,
                false);
        controller.draw(currentPlayer.getNickname(),
                'R',
                0
        );
        // THIRD TURN
        currentPlayer = board.getCurrentPlayer();
        board.addScore(currentPlayer, 20);
        controller.placeCard(currentPlayer.getNickname(),
                currentPlayer.getHand().getCard(0).getCardID(),
                new GamePoint(0,0),
                CornerDirection.TL,
                false
        );

        controller.draw(currentPlayer.getNickname(),
                'R',
                0
        );
        //FIRST TURN
        currentPlayer = board.getCurrentPlayer();
        controller.placeCard(currentPlayer.getNickname(),
                currentPlayer.getHand().getCard(0).getCardID(),
                new GamePoint(0,0),
                CornerDirection.TL,
                false);
        controller.draw(currentPlayer.getNickname(),
                'R',
                0
        );
        // SECOND TURN
        currentPlayer = board.getCurrentPlayer();
        controller.placeCard(currentPlayer.getNickname(),
                currentPlayer.getHand().getCard(0).getCardID(),
                new GamePoint(0,0),
                CornerDirection.BL,
                false);
        controller.draw(currentPlayer.getNickname(),
                'R',
                0
        );
    }
    private void playLastTurn(){
        // THIRD TURN
        Player currentPlayer = board.getCurrentPlayer();
        controller.placeCard(currentPlayer.getNickname(),
                currentPlayer.getHand().getCard(0).getCardID(),
                new GamePoint(0,0),
                CornerDirection.BL,
                false);
        controller.draw(currentPlayer.getNickname(),
                'R',
                0
        );

        assertEquals(EndgameState.class, controller.getGameState().getClass());
    }


    // Tested exceptions can be thrown whether the player nickname isn't recognised
    // (due to the player not joining the game beforehand) or if the command
    // cannot be processed in the current state.

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
        assertThrows(
                IllegalArgumentException.class,
                () -> controller.disconnect("Player 4")
        );
        controller.disconnect("Player 3");
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
                () -> controller.placeCard("Donnina bella", "SUS", new GamePoint(69,69),
                        CornerDirection.TR, true)
        );

    }

    @Test
    void startGame() {
        assertThrows(
          IllegalArgumentException.class,
                () -> controller.restartGame(playerNickname, 2)
        );
        controller.restartGame(playerNickname, 4);
        assertEquals(JoinState.class, controller.getGameState().getClass());
        controller.join("Player 4", new PuppetClient());
        assertEquals(SetupState.class, controller.getGameState().getClass());
    }

    @Test
    void startGameWhileDisconnecting() throws InterruptedException {
        new Thread(
                () ->controller.disconnect("Player 3")
        ).start();
        Thread startingThread = new Thread(
                () ->{
                    try {
                        controller.restartGame(playerNickname, 3);
                    } catch (IllegalStateException ignore){
                    }
                }
        );
        startingThread.start();
        controller.disconnect(secondPlNick);
        startingThread.join();
        boolean gameRestarted = false;
        while (!gameRestarted){
            try{
                if(controller.getGameState().getClass() == JoinState.class) {
                    break;
                }
                controller.restartGame(playerNickname, 3);
                gameRestarted = true;
            } catch (IllegalStateException exception){
                System.err.println(exception.getMessage());
            }
        }
        assertEquals(JoinState.class, controller.getGameState().getClass());
    }

    @Test
    void restartGameWithFullLobby() {
        controller.restartGame(playerNickname, 3);
        assertEquals(SetupState.class, controller.getGameState().getClass());
    }

    @Test
    void restartWhenAllDisconnected(){
        controller.disconnect(playerNickname);
        controller.disconnect(secondPlNick);
        controller.disconnect("Player 3");
        assertEquals(CreationState.class, controller.getGameState().getClass());
    }

    @Test
    void testTies(){
        init();
        joinUntilSetupState(3);
        playUntilPlayState();
        playUntilLastTurn();

        //now manually setup the tie:
        List<Player> playersByScore = board.getPlayersByScore();

        //reset scores
        playersByScore.forEach(
                p -> board.addScore(p, -board.getScoreboard().get(p))
        );

        //set top scores
        board.addScore(playersByScore.get(0), 5);
        board.addScore(playersByScore.get(1), 5);

        displayScores(playersByScore);
        playersByScore.forEach(this::displayVisibleRes);
        System.out.println();

        board.getRevealedObjectives().forEach(
                obj -> System.out.println("Final objective: " + obj)
        );
        System.out.println("EXECUTE LAST TURN");
        playLastTurn();

        displayScores(playersByScore);
    }
    private void displayScores(List<Player> playersByScore){
        playersByScore.forEach(
                p -> System.out.println(p.getNickname() + " -> " + board.getScoreboard().get(p))
        );
    }
    private void displayVisibleRes(Player player){
        Map<GameResource, Integer> visibleRes = board.getPlayerAreas().get(player).getVisibleResources();
        System.out.println("Visible Resources of " + player.getNickname());
        visibleRes.keySet().forEach(
                res ->{
                    if(visibleRes.get(res)>0)
                        System.out.println(res + " = " + visibleRes.get(res));
                }
        );
        System.out.println();
    }
}