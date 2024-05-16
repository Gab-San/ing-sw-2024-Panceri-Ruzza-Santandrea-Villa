package it.polimi.ingsw.controller;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.controller.testclass.PuppetClient;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.PlayArea;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.GameResource;
import it.polimi.ingsw.model.enums.PlayerColor;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;


//TODO QUESTO TEST VA COMPLETATO

public class PlayStateTest{
    private BoardController controller;
    private Board board;
    private final String playerNickname = "Flavio";

    private List<char[]> drawableCards;

    public void setUp(int numOfPlayers){
        controller = new BoardController("Flavio's Game");
        controller.join(playerNickname, new PuppetClient());
        controller.setNumOfPlayers(playerNickname, numOfPlayers);
        board = controller.getGameState().board;
        joinUntilSetupState(numOfPlayers);
        placeAllStarting(numOfPlayers);
        giveAllColors(numOfPlayers);
        giveAllSecretObjectives(numOfPlayers);
        drawableCards=new ArrayList<>();
        drawableCards.add(new char[]{'R','1'});
        drawableCards.add(new char[]{'R','2'});
        drawableCards.add(new char[]{'R','0'});
        drawableCards.add(new char[]{'G','1'});
        drawableCards.add(new char[]{'G','2'});
        drawableCards.add(new char[]{'G','0'});
    }
    public void joinUntilSetupState(int numOfPlayers) {
        for (int j = 2; j <= numOfPlayers; j++) {
            controller.join("Player " + j, new PuppetClient());
        }
    }
    private void placeAllStarting(int numOfPlayers) {
        for (Player p : board.getPlayerAreas().keySet()) {
            controller.placeStartingCard(p.getNickname(), new Random().nextBoolean());
        }
    }
    private void giveAllColors(int numOfPlayers) {
        for (Player p : board.getPlayerAreas().keySet()) {
            controller.chooseYourColor(p.getNickname(), board.getRandomAvailableColor());
        }
    }
    private void giveAllSecretObjectives(int numOpPlayers){
        for(Player p : board.getPlayerAreas().keySet()){
            controller.chooseSecretObjective(p.getNickname(), new Random().nextInt(2)+1);
        }
    }

    @ParameterizedTest
    @ValueSource(ints={2,3,4})
    public void joinTest(int numOfPlayers) {

    }

    @ParameterizedTest
    @ValueSource(ints={2,3,4})
    public void setNumOfPlayersTest(int numOfPlayers) {
        setUp(numOfPlayers);
        assertThrows(IllegalStateException.class, () -> controller.setNumOfPlayers("definitelyNotRight", new Random().nextInt(3)+2), "SetNumOfPlayers doesn't throw IllegalStateException wrong nickname");
        assertThrows(IllegalStateException.class, () -> controller.setNumOfPlayers(playerNickname, new Random().nextInt(3)+2), "SetNumOfPlayers doesn't throw IllegalStateException");
    }

    @ParameterizedTest
    @ValueSource(ints={2,3,4})
    public void placeStartingCardTest(int numOfPlayers) {
        setUp(numOfPlayers);
        assertThrows(IllegalStateException.class, () -> controller.placeStartingCard("definitelyNotRight", true), "PlaceStartingCard doesn't throw IllegalStateException with wrong nickname");
        assertThrows(IllegalStateException.class, () -> controller.placeStartingCard(playerNickname, true), "PlaceStartingCard doesn't throw IllegalStateException with placeOnFront==true");
        assertThrows(IllegalStateException.class, () -> controller.placeStartingCard(playerNickname, false), "PlaceStartingCard doesn't throw IllegalStateException with placeOnFront==false");
    }

    @ParameterizedTest
    @ValueSource(ints={2,3,4})
    public void chooseYourColorTest(int numOfPlayers) {
        setUp(numOfPlayers);
        assertThrows(IllegalStateException.class, () -> controller.chooseYourColor("definitelyNotRight", PlayerColor.BLUE),"ChooseYourColor doesn't throw IllegalStateException with wrong nickname");
        assertThrows(IllegalStateException.class, () -> controller.chooseYourColor(playerNickname, PlayerColor.BLUE),"ChooseYourColor doesn't throw IllegalStateException with color==BLUE");
        assertThrows(IllegalStateException.class, () -> controller.chooseYourColor(playerNickname, PlayerColor.RED),"ChooseYourColor doesn't throw IllegalStateException with color==RED");
        assertThrows(IllegalStateException.class, () -> controller.chooseYourColor(playerNickname, PlayerColor.GREEN),"ChooseYourColor doesn't throw IllegalStateException with color==GREEN");
        assertThrows(IllegalStateException.class, () -> controller.chooseYourColor(playerNickname, PlayerColor.YELLOW),"ChooseYourColor doesn't throw IllegalStateException with color==YELLOW");
    }

    @ParameterizedTest
    @ValueSource(ints={2,3,4})
    public void chooseSecretObjectiveTest(int numOfPlayers) {
        setUp(numOfPlayers);
        assertThrows(IllegalStateException.class, () -> controller.chooseSecretObjective("definitelyNotRight", 0), "ChooseSecretObjective doesn't throw IllegalStateException with wrong nickname");
        assertThrows(IllegalStateException.class, () -> controller.chooseSecretObjective(playerNickname, 0), "ChooseSecretObjective doesn't throw IllegalStateException with choice==0");
        assertThrows(IllegalStateException.class, () -> controller.chooseSecretObjective(playerNickname, 1), "ChooseSecretObjective doesn't throw IllegalStateException with choice==1");
    }

    @ParameterizedTest
    @ValueSource(ints={2,3,4})
    public void placeCardTest(int numOfPlayers) {
        setUp(numOfPlayers);
        assertEquals(GamePhase.PLACECARD,board.getGamePhase());
        assertEquals(PlayState.class, controller.getGameState().getClass());

        assertThrows(IndexOutOfBoundsException.class, ()-> placeRandomCard(new Player("definitelyNotRight")), "PlaceCard does not throws IllegalArgumentException with wrong nickname");
        assertThrows(IllegalStateException.class, ()-> placeRandomCard(Objects.requireNonNull(board.getPlayerAreas().keySet().stream().filter((p) -> !p.equals(board.getCurrentPlayer())).findAny().orElse(null))),
                "PlaceCard does not throws IllegalArgumentException with wrong nickname");

        Player currPlayer=board.getCurrentPlayer();
        int turn= board.getCurrentTurn();
        try{
            assertEquals(GamePhase.PLACECARD,board.getGamePhase());
            assertEquals(PlayState.class, controller.getGameState().getClass());
            placeRandomCard(currPlayer);
        }catch (IllegalStateException | IllegalArgumentException e) {fail("PlaceRandomCard throws IllegalStateException when it shouldn't: " +e.getMessage());}

        assertEquals(GamePhase.DRAWCARD,board.getGamePhase());
        assertEquals(PlayState.class, controller.getGameState().getClass());
        assertEquals(board, controller.getGameState().board);

        assertEquals(turn, board.getCurrentTurn(), "Turn did change: turn="+ turn +" and nextTurn="+board.getCurrentTurn());
        assertEquals(currPlayer, board.getCurrentPlayer());
    }
    private void placeRandomCard(Player player) throws IllegalStateException, IllegalArgumentException{
        PlayArea currPlayArea=board.getPlayerAreas().get(player);
        boolean flipped=new Random().nextBoolean();
        String cardID=null;
        boolean found=false;
        //controllo se la carta può essere posizionata rispetto alle sue risorse necessarie, in caso ne cerco un'altra
        while (!found){
            found=true;
            PlayCard card=player.getHand().getCard(new Random().nextInt(3));
            if(flipped)
                card.turnFaceUp();
            else card.turnFaceDown();
            for (GameResource r : card.getPlacementCost().keySet()){
                if (currPlayArea.getVisibleResources().get(r) == null ||
                        currPlayArea.getVisibleResources().get(r) < card.getPlacementCost().get(r)){
                    found=false;
                }
            }
            if(found)
                cardID=card.getCardID();
            else flipped=new Random().nextBoolean();
        }

        Corner corner=currPlayArea.getFreeCorners().get(new Random ().nextInt(currPlayArea.getFreeCorners().size()));
        Point pos=corner.getCardRef().getPosition();

        assertNotEquals(null, pos, "there's an error somewhere"); //should never be happening but to pr
        controller.placeCard(player.getNickname(), cardID, pos, corner.getDirection(), flipped);
    }


    @ParameterizedTest
    @ValueSource(ints={2,3,4})
    public void drawTest(int numOfPlayers) {
        setUp(numOfPlayers);
        placeRandomCard(board.getCurrentPlayer());
        assertEquals(GamePhase.DRAWCARD,board.getGamePhase());
        assertEquals(PlayState.class, controller.getGameState().getClass());

        assertThrows(IllegalArgumentException.class, ()-> drawRandomCard(new Player("definitelyNotRight")), "DrawCard does not throws IllegalArgumentException with wrong nickname");
        assertThrows(IllegalStateException.class, ()-> drawRandomCard(Objects.requireNonNull(board.getPlayerAreas().keySet().stream().filter((p) -> !p.equals(board.getCurrentPlayer())).findAny().orElse(null))),
                "PlaceCard does not throws IllegalArgumentException with wrong nickname");

        Player currPlayer=board.getCurrentPlayer();
        int turn= board.getCurrentTurn();
        try{
            assertEquals(GamePhase.DRAWCARD,board.getGamePhase());
            assertEquals(PlayState.class, controller.getGameState().getClass());
            drawRandomCard(currPlayer);
        }catch (IllegalStateException | IllegalArgumentException e) {
            fail("DrawRandomCard throws IllegalStateException when it shouldn't: "+ e.getMessage());
        }

        assertEquals(GamePhase.PLACECARD,board.getGamePhase());
        assertEquals(PlayState.class, controller.getGameState().getClass());
        assertEquals(board, controller.getGameState().board);
        assertEquals(turn==numOfPlayers ? 1 : turn+1, board.getCurrentTurn(), "Turn did change: turn="+ turn +" and nextTurn="+board.getCurrentTurn());
        assertEquals(board.getCurrentPlayer(), board.getPlayersByTurn().get(currPlayer.getTurn()==numOfPlayers ? 0 : currPlayer.getTurn()));
    }

    private void drawRandomCard(Player player){
        System.err.println("drawableCards.size()=" + drawableCards.size());
        char[] cardToDraw = drawableCards.get(new Random().nextInt(drawableCards.size()));
        if(!player.equals(board.getCurrentPlayer()))
            controller.draw(player.getNickname(), cardToDraw[0], Character.getNumericValue(cardToDraw[1]));
        else {
            try {
                controller.draw(player.getNickname(), cardToDraw[0], Character.getNumericValue(cardToDraw[1]));
            } catch (/*DeckException |*/ IllegalArgumentException | IllegalStateException e) {
                drawableCards.remove(cardToDraw);
                drawRandomCard(player);
            }
        }
    }

    @ParameterizedTest
    @ValueSource(ints={2,3,4})
    public void startGameTest(int numOfPlayers) {
        setUp(numOfPlayers);
        assertThrows(IllegalStateException.class, () -> controller.restartGame("definitelyNotRight", 2), "StartGame doesn't throw IllegalStateException with wrong nickname");
        assertThrows(IllegalStateException.class, () -> controller.restartGame(playerNickname, 2), "StartGame doesn't throw IllegalStateException with numOfPlayers==2");
        assertThrows(IllegalStateException.class, () -> controller.restartGame(playerNickname, 3), "StartGame doesn't throw IllegalStateException with numOfPlayers==3");
        assertThrows(IllegalStateException.class, () -> controller.restartGame(playerNickname, 4), "StartGame doesn't throw IllegalStateException with numOfPlayers==4");
    }

    @ParameterizedTest
    @ValueSource(ints = {2,3,4})
    public void simulateRandomGame(int numOfPlayers){
        setUp(numOfPlayers);
        assertEquals(GamePhase.PLACECARD,board.getGamePhase());
        assertEquals(PlayState.class, controller.getGameState().getClass());

        boolean endgame=false;
        boolean isLastTurn=false;
        int i=1;
        List<Player> playersByTurn=board.getPlayersByTurn();
        List<String> nicknamesByTurn=playersByTurn.stream().map(Player::getNickname).toList();
        System.err.println(nicknamesByTurn);

        while(EndgameState.class != controller.getGameState().getClass()) {
            Player currPlayer = board.getCurrentPlayer();
            int turn = board.getCurrentTurn();
            System.err.println("il turno è "+i);
            System.err.println("currPlayer is " + currPlayer.getNickname());

            System.err.println("I'M PLACING");
            assertEquals(board.getCurrentPlayer(), currPlayer, "currPlayer is " + currPlayer.getNickname() +" instead of "+board.getCurrentPlayer());
            placeRandomCard(currPlayer);

            if(GamePhase.SHOWWIN==board.getGamePhase())
                    break;
            if (!board.canDraw()) {
                System.err.println("board can draw="+board.canDraw());
                assertEquals(GamePhase.PLACECARD, board.getGamePhase());
                assertEquals(PlayState.class, controller.getGameState().getClass());
                assertEquals(board, controller.getGameState().board);
                assertEquals(turn==numOfPlayers ? 1 : turn + 1, board.getCurrentTurn(), "Turn did change: turn=" + turn + " and nextTurn=" + board.getCurrentTurn());
                assertEquals(board.getCurrentPlayer(), board.getPlayersByTurn().get(currPlayer.getTurn() == numOfPlayers ? 0 : currPlayer.getTurn()));
            }
            else{
                System.err.println("can draw: "+board.canDraw());

                assertEquals(GamePhase.DRAWCARD, board.getGamePhase(), "WRONG AFTER PLACE");
                assertEquals(PlayState.class, controller.getGameState().getClass());
                assertEquals(board, controller.getGameState().board);
                assertEquals(turn, board.getCurrentTurn(), "Turn did change: turn=" + turn + " and nextTurn=" + board.getCurrentTurn());
                assertEquals(currPlayer, board.getCurrentPlayer());

                System.err.println("I'M DRAWING");
                drawRandomCard(currPlayer);

                if(GamePhase.SHOWWIN==board.getGamePhase())
                    break;
                assertEquals(GamePhase.PLACECARD, board.getGamePhase(), "WRONG AFTER DRAW");
                assertEquals(PlayState.class, controller.getGameState().getClass());
                assertEquals(board, controller.getGameState().board);
                assertEquals(turn == numOfPlayers ? 1 : turn + 1, board.getCurrentTurn(), "Turn did change: turn=" + turn + " and nextTurn=" + board.getCurrentTurn());
                assertEquals(board.getCurrentPlayer(), board.getPlayersByTurn().get(currPlayer.getTurn() == numOfPlayers ? 0 : currPlayer.getTurn()));


                if (isLastTurn && turn == 1) {
                    assertEquals(GamePhase.SHOWWIN, board.getGamePhase());
                    assertEquals(EndgameState.class, controller.getGameState().getClass());
                } else {
                    assertEquals(GamePhase.PLACECARD, board.getGamePhase());
                    assertEquals(PlayState.class, controller.getGameState().getClass());
                    assertEquals(board, controller.getGameState().board);
                    assertEquals(turn == numOfPlayers ? 1 : turn + 1, board.getCurrentTurn(), "Turn did change: turn=" + turn + " and nextTurn=" + board.getCurrentTurn());
                    assertEquals(board.getCurrentPlayer(), board.getPlayersByTurn().get(currPlayer.getTurn() == numOfPlayers ? 0 : currPlayer.getTurn()));
                }

                if (endgame && turn == 1)
                    isLastTurn = true;
                if (board.checkEndgame() && turn == numOfPlayers)
                    endgame = true;
                System.err.println("finished turn " + ((i - 1) % numOfPlayers + 1) + " of the set n° " + (int) (i - 1) / numOfPlayers);
                i++;
            }
        }

        assertEquals(GamePhase.SHOWWIN, board.getGamePhase());
        assertEquals(EndgameState.class, controller.getGameState().getClass());
    }
}
