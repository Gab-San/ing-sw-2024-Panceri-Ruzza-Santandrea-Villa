package it.polimi.ingsw.controller;

import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.PlayArea;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.stub.PuppetClient;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;


public class PlayStateTest {
    private BoardController controller;
    private Board board;
    private final String playerNickname = "Flavio";

    private List<char[]> drawableCards;

    public void setUp(int numOfPlayers) {
        controller = new BoardController();
        controller.join(playerNickname, new PuppetClient());
        controller.setNumOfPlayers(playerNickname, numOfPlayers);
        board = controller.getGameState().board;
        joinUntilSetupState(numOfPlayers);
        placeAllStarting();
        giveAllColors();
        giveAllSecretObjectives();
        drawableCards = new ArrayList<>();
        drawableCards.add(new char[]{'R', '1'});
        drawableCards.add(new char[]{'R', '2'});
        drawableCards.add(new char[]{'R', '0'});
        drawableCards.add(new char[]{'G', '1'});
        drawableCards.add(new char[]{'G', '2'});
        drawableCards.add(new char[]{'G', '0'});
    }

    public void joinUntilSetupState(int numOfPlayers) {
        for (int j = 2; j <= numOfPlayers; j++) {
            controller.join("Player " + j, new PuppetClient());
        }
    }

    private void placeAllStarting() {
        for (Player p : board.getPlayerAreas().keySet()) {
            controller.placeStartingCard(p.getNickname(), new Random().nextBoolean());
        }
    }

    private void giveAllColors() {
        for (Player p : board.getPlayerAreas().keySet()) {
            controller.chooseYourColor(p.getNickname(), board.getRandomAvailableColor());
        }
    }

    private void giveAllSecretObjectives() {
        for (Player p : board.getPlayerAreas().keySet()) {
            controller.chooseSecretObjective(p.getNickname(), new Random().nextInt(2) + 1);
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3, 4})
    public void setNumOfPlayersTest(int numOfPlayers) {
        setUp(numOfPlayers);
        assertThrows(IllegalStateException.class, () -> controller.setNumOfPlayers("definitelyNotRight", new Random().nextInt(3) + 2), "SetNumOfPlayers doesn't throw IllegalStateException wrong nickname");
        assertThrows(IllegalStateException.class, () -> controller.setNumOfPlayers(playerNickname, new Random().nextInt(3) + 2), "SetNumOfPlayers doesn't throw IllegalStateException");
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3, 4})
    public void placeStartingCardTest(int numOfPlayers) {
        setUp(numOfPlayers);
        assertThrows(IllegalStateException.class, () -> controller.placeStartingCard("definitelyNotRight", true), "PlaceStartingCard doesn't throw IllegalStateException with wrong nickname");
        assertThrows(IllegalStateException.class, () -> controller.placeStartingCard(playerNickname, true), "PlaceStartingCard doesn't throw IllegalStateException with placeOnFront==true");
        assertThrows(IllegalStateException.class, () -> controller.placeStartingCard(playerNickname, false), "PlaceStartingCard doesn't throw IllegalStateException with placeOnFront==false");
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3, 4})
    public void chooseYourColorTest(int numOfPlayers) {
        setUp(numOfPlayers);
        assertThrows(IllegalStateException.class, () -> controller.chooseYourColor("definitelyNotRight", PlayerColor.BLUE), "ChooseYourColor doesn't throw IllegalStateException with wrong nickname");
        assertThrows(IllegalStateException.class, () -> controller.chooseYourColor(playerNickname, PlayerColor.BLUE), "ChooseYourColor doesn't throw IllegalStateException with color==BLUE");
        assertThrows(IllegalStateException.class, () -> controller.chooseYourColor(playerNickname, PlayerColor.RED), "ChooseYourColor doesn't throw IllegalStateException with color==RED");
        assertThrows(IllegalStateException.class, () -> controller.chooseYourColor(playerNickname, PlayerColor.GREEN), "ChooseYourColor doesn't throw IllegalStateException with color==GREEN");
        assertThrows(IllegalStateException.class, () -> controller.chooseYourColor(playerNickname, PlayerColor.YELLOW), "ChooseYourColor doesn't throw IllegalStateException with color==YELLOW");
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3, 4})
    public void chooseSecretObjectiveTest(int numOfPlayers) {
        setUp(numOfPlayers);
        assertThrows(IllegalStateException.class, () -> controller.chooseSecretObjective("definitelyNotRight", 0), "ChooseSecretObjective doesn't throw IllegalStateException with wrong nickname");
        assertThrows(IllegalStateException.class, () -> controller.chooseSecretObjective(playerNickname, 0), "ChooseSecretObjective doesn't throw IllegalStateException with choice==0");
        assertThrows(IllegalStateException.class, () -> controller.chooseSecretObjective(playerNickname, 1), "ChooseSecretObjective doesn't throw IllegalStateException with choice==1");
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3, 4})
    public void placeCardTest(int numOfPlayers) {
        setUp(numOfPlayers);
        assertEquals(GamePhase.PLACECARD, board.getGamePhase());
        assertEquals(PlayState.class, controller.getGameState().getClass());

        assertThrows(IndexOutOfBoundsException.class, () -> placeRandomCard(new Player("definitelyNotRight")), "PlaceCard does not throws IllegalArgumentException with wrong nickname");
        assertThrows(IllegalStateException.class, () -> placeRandomCard(Objects.requireNonNull(board.getPlayerAreas().keySet().stream().filter((p) -> !p.equals(board.getCurrentPlayer())).findAny().orElse(null))),
                "PlaceCard does not throws IllegalArgumentException with wrong nickname");

        Player currPlayer = board.getCurrentPlayer();
        int turn = board.getCurrentTurn();
        try {
            assertEquals(GamePhase.PLACECARD, board.getGamePhase());
            assertEquals(PlayState.class, controller.getGameState().getClass());
            placeRandomCard(currPlayer);
        } catch (IllegalStateException | IllegalArgumentException e) {
            fail("PlaceRandomCard throws IllegalStateException when it shouldn't: " + e.getMessage());
        }

        assertEquals(GamePhase.DRAWCARD, board.getGamePhase());
        assertEquals(PlayState.class, controller.getGameState().getClass());
        assertEquals(board, controller.getGameState().board);

        assertEquals(turn, board.getCurrentTurn(), "Turn did change: turn=" + turn + " and nextTurn=" + board.getCurrentTurn());
        assertEquals(currPlayer, board.getCurrentPlayer());
    }

    private void placeRandomCard(Player player) throws IllegalStateException, IllegalArgumentException {
        PlayArea currPlayArea = board.getPlayerAreas().get(player);
        boolean flipped = new Random().nextBoolean();
        String cardID = null;
        boolean found = false;
        //controllo se la carta può essere posizionata rispetto alle sue risorse necessarie, in caso ne cerco un'altra
        while (!found) {
            found = true;
            int numOfCardsInHand = player.getHand().isHandFull() ? 3 : 2;
            PlayCard card = player.getHand().getCard(new Random().nextInt(numOfCardsInHand));
            if (flipped)
                card.turnFaceUp();
            else card.turnFaceDown();
            for (GameResource r : card.getPlacementCost().keySet()) {
                if (currPlayArea.getVisibleResources().get(r) == null ||
                        currPlayArea.getVisibleResources().get(r) < card.getPlacementCost().get(r)) {
                    found = false;
                }
            }
            if (found)
                cardID = card.getCardID();
            else flipped = new Random().nextBoolean();
        }

        Corner corner = currPlayArea.getFreeCorners().get(new Random().nextInt(currPlayArea.getFreeCorners().size()));
        GamePoint pos = corner.getCardRef().getPosition();

        assertNotEquals(null, pos, "there's an error somewhere"); //should never be happening but to pr
        controller.placeCard(player.getNickname(), cardID, pos, corner.getDirection(), flipped);
    }


    @ParameterizedTest
    @ValueSource(ints = {2, 3, 4})
    public void drawTest(int numOfPlayers) {
        setUp(numOfPlayers);
        placeRandomCard(board.getCurrentPlayer());
        assertEquals(GamePhase.DRAWCARD, board.getGamePhase());
        assertEquals(PlayState.class, controller.getGameState().getClass());

        assertThrows(IllegalArgumentException.class, () -> drawRandomCard(new Player("definitelyNotRight")), "DrawCard does not throws IllegalArgumentException with wrong nickname");
        assertThrows(IllegalStateException.class, () -> drawRandomCard(Objects.requireNonNull(board.getPlayerAreas().keySet().stream().filter((p) -> !p.equals(board.getCurrentPlayer())).findAny().orElse(null))),
                "PlaceCard does not throws IllegalArgumentException with wrong nickname");

        Player currPlayer = board.getCurrentPlayer();
        int turn = board.getCurrentTurn();
        try {
            assertEquals(GamePhase.DRAWCARD, board.getGamePhase());
            assertEquals(PlayState.class, controller.getGameState().getClass());
            drawRandomCard(currPlayer);
        } catch (IllegalStateException | IllegalArgumentException e) {
            fail("DrawRandomCard throws IllegalStateException when it shouldn't: " + e.getMessage());
        }

        assertEquals(GamePhase.PLACECARD, board.getGamePhase());
        assertEquals(PlayState.class, controller.getGameState().getClass());
        assertEquals(board, controller.getGameState().board);
        assertEquals(turn == numOfPlayers ? 1 : turn + 1, board.getCurrentTurn(), "Turn did change: turn=" + turn + " and nextTurn=" + board.getCurrentTurn());
        assertEquals(board.getCurrentPlayer(), board.getPlayersByTurn().get(currPlayer.getTurn() == numOfPlayers ? 0 : currPlayer.getTurn()));
    }

    private void drawRandomCard(Player player) {

//        System.err.println("drawableCards.size()=" + drawableCards.size());
        char[] cardToDraw = drawableCards.get(new Random().nextInt(drawableCards.size()));
        if (!player.equals(board.getCurrentPlayer()))
            controller.draw(player.getNickname(), cardToDraw[0], Character.getNumericValue(cardToDraw[1]));
        else {
            try {
                controller.draw(player.getNickname(), cardToDraw[0], Character.getNumericValue(cardToDraw[1]));
            } catch (IllegalArgumentException | IllegalStateException e) {
                drawableCards.remove(cardToDraw);
                System.err.println("drawableCards.size()=" + drawableCards.size());
                drawRandomCard(player);
            }
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3, 4})
    public void startGameTest(int numOfPlayers) {
        setUp(numOfPlayers);
        assertThrows(IllegalStateException.class, () -> controller.restartGame("definitelyNotRight", 2), "StartGame doesn't throw IllegalStateException with wrong nickname");
        assertThrows(IllegalStateException.class, () -> controller.restartGame(playerNickname, 2), "StartGame doesn't throw IllegalStateException with numOfPlayers==2");
        assertThrows(IllegalStateException.class, () -> controller.restartGame(playerNickname, 3), "StartGame doesn't throw IllegalStateException with numOfPlayers==3");
        assertThrows(IllegalStateException.class, () -> controller.restartGame(playerNickname, 4), "StartGame doesn't throw IllegalStateException with numOfPlayers==4");
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3, 4})
    //@RepeatedTest(200)
    public void simulateRandomGame(int numOfPlayers) {
        setUp(/*4*/numOfPlayers);
        //int numOfPlayers=4;
        assertEquals(GamePhase.PLACECARD, board.getGamePhase());
        assertEquals(PlayState.class, controller.getGameState().getClass());

        boolean endgame = false;
        boolean isLastTurn; // will be true if last turn of last round
        int i = 1;
        List<Player> playersByTurn = board.getPlayersByTurn();
        List<String> nicknamesByTurn = playersByTurn.stream().map(Player::getNickname).toList();
        System.err.println(nicknamesByTurn);
        Player lastPlayer;

        while (true) { //isLastTurn == true is the break condition, done manually inside the loop
            //setup test single turn
            Player currPlayer = board.getCurrentPlayer();
            lastPlayer = currPlayer;
            System.err.println("endgame=" + endgame + "\n\n");

            isLastTurn = controlIsLastTurn(currPlayer, endgame);

            System.err.println("il turno è " + i);
            System.err.println("started turn " + ((i - 1) % numOfPlayers + 1) + " of the set n° " + (i - 1) / numOfPlayers);

            System.err.println("currPlayer is " + currPlayer.getNickname());

            System.err.println("I'M PLACING");
            assertEquals(board.getCurrentPlayer(), currPlayer, "currPlayer is " + currPlayer.getNickname() + " instead of " + board.getCurrentPlayer());
            placeRandomCard(currPlayer);

            System.err.println("isLastTurn=" + isLastTurn);

            if (!board.canDraw()) {
                if (isLastTurn)
                    break;

                System.err.println("board can draw=" + board.canDraw());
                controlPostPlaceCantDraw();
            } else { //if I can still draw
                System.err.println("can draw: " + board.canDraw());
                controlPostPlaceDrawable();

                System.err.println("I'M DRAWING");
                drawRandomCard(currPlayer);

                if (isLastTurn)
                    break;

                controlPostDraw();

                System.err.println("finished turn " + ((i - 1) % numOfPlayers + 1) + " of the set n° " + (i - 1) / numOfPlayers);
            }

            endgame = controlEndgame(currPlayer, endgame);
            System.err.println(">>>>>>>>>>>>>>>>>CHECK END GAME IS " + board.checkEndgame() + "<<<<<<<<<<<<<<<<<");

            i++;

        }

        assertEquals(lastPlayer, getLastActivePlayer());
        System.err.println("last player is " + lastPlayer.getNickname());
        System.err.println("finito al turno " + i);
        assertTrue(endgame, "endgame may be false");
//        assertTrue(isLastTurn, "isLastTurn is " + isLastTurn);  last turn always true
        assertEquals(GamePhase.SHOWWIN, board.getGamePhase());
        assertEquals(EndgameState.class, controller.getGameState().getClass());
    }

    private void controlPostPlaceDrawable() {
        assertEquals(GamePhase.DRAWCARD, board.getGamePhase(), "WRONG AFTER PLACE");
        assertEquals(PlayState.class, controller.getGameState().getClass());
        assertEquals(board, controller.getGameState().board);
    }

    /**
     * PARAM gamePhase IN PLAYCARD: IF CAN'T DRAW AFTER (GamePhase.PLACECARD), IF CAN DRAW AFTER (GamePhase.DRAWCARD)
     * PARAM gamePhase IN DRAW: ALWAYS (GamePhase.PLACECARD)
     */
    private void controlPostPlaceCantDraw() {
        assertEquals(GamePhase.PLACECARD, board.getGamePhase());
        assertEquals(PlayState.class, controller.getGameState().getClass());
        assertEquals(board, controller.getGameState().board);
    }

    private void controlPostDraw() {
        assertEquals(GamePhase.PLACECARD, board.getGamePhase(), "WRONG AFTER DRAW");
        assertEquals(PlayState.class, controller.getGameState().getClass());
        assertEquals(board, controller.getGameState().board);
    }

    private Player getLastActivePlayer() {
        List<Player> activePlayers = board.getPlayersByTurn().stream()
                .filter((p) -> p.isConnected() && !board.getPlayerDeadlocks().get(p)).toList();
        if(activePlayers.isEmpty())
            return null;
        return activePlayers.get(activePlayers.size() - 1);
    }

    private boolean controlIsLastTurn(Player currPlayer, boolean endgame) {
        boolean isLastPlayerTurn =  (getLastActivePlayer()==null) || getLastActivePlayer().equals(currPlayer);
        return (isLastPlayerTurn && endgame);
    }
    private boolean controlEndgame(Player currPlayer, boolean endgame) {
        boolean isLastPlayerTurn = getLastActivePlayer()==null || getLastActivePlayer().equals(currPlayer);
        return (isLastPlayerTurn && board.checkEndgame()) || endgame;
    }
    private boolean isDisconnectingTurn(int i, int disconnectTurn){
        return (i == disconnectTurn) && new Random().nextBoolean()
                && board.getNumOfConnectedPlayers() > 2;
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3, 4})
    public void simulateRandomGameWithDisconnect(int numOfPlayers) {
        setUp(numOfPlayers);

        assertEquals(GamePhase.PLACECARD, board.getGamePhase());
        assertEquals(PlayState.class, controller.getGameState().getClass());

        boolean endgame = false;
        boolean isLastTurn = false;
        int i = 1;
        List<Player> playersByTurn = board.getPlayersByTurn();
        List<String> nicknamesByTurn = playersByTurn.stream().map(Player::getNickname).toList();
        System.err.println(nicknamesByTurn);
        Player lastPlayer = null;

        //disconnect part:
        int disconnectTurn =new Random().nextInt(100);

        System.err.println("disconnecting turn is "+disconnectTurn);

        while (!isLastTurn) {
            int numOfActivePlayers = board.getNumOfConnectedPlayers();

            //setup test single turn
            Player currPlayer = board.getCurrentPlayer();
            lastPlayer = currPlayer;
            int turn = board.getCurrentTurn();
            System.err.println("endgame=" + endgame + "\n\n");


            System.err.println("il turno è " + i);
            System.err.println("started turn " + turn + " of the set n° " + (i - 1) / numOfActivePlayers);

            System.err.println("currPlayer is " + currPlayer.getNickname());

            System.err.println("I'M PLACING");
            assertEquals(board.getCurrentPlayer(), currPlayer, "currPlayer is " + currPlayer.getNickname() + " instead of " + board.getCurrentPlayer().getNickname());

            //disconnect part
            if (isDisconnectingTurn(i, disconnectTurn)){
                isLastTurn = controlIsLastTurn(currPlayer, endgame);
                endgame = controlEndgame(currPlayer, endgame);

                System.err.println("disconnecting before placing card");
                controller.disconnect(currPlayer.getNickname());
                if(board.getGamePhase()==GamePhase.SHOWWIN)
                    break;
            }
            else {
                placeRandomCard(currPlayer);
                if (isDisconnectingTurn(i, disconnectTurn)){
                    isLastTurn = controlIsLastTurn(currPlayer, endgame);
                    endgame = controlEndgame(currPlayer, endgame);

                    System.err.println("disconnecting after placing card");
                    controller.disconnect(currPlayer.getNickname());
                    if(board.getGamePhase()==GamePhase.SHOWWIN)
                        break;
                }

                else {
                    isLastTurn = controlIsLastTurn(currPlayer, endgame);
                    System.err.println("isLastTurn=" + isLastTurn);

                    if (!board.canDraw()) {
                        isLastTurn = controlIsLastTurn(currPlayer, endgame);
                        //if this is the last turn of the last round
                        if (isLastTurn)
                            break;

                        //if this is not the last turn of the last round
                        System.err.println("board can draw=" + board.canDraw());

                        controlPostPlaceCantDraw();
                    } else { //if I can draw
                        System.err.println("can draw: " + board.canDraw());
                        //check if this is drawPhase (if I can draw I must pass through here to end the game)
                        controlPostPlaceDrawable();

                        System.err.println("I'M DRAWING");

                        drawRandomCard(currPlayer);
                        if (isDisconnectingTurn(i, disconnectTurn)) {
                            isLastTurn = controlIsLastTurn(currPlayer, endgame);
                            endgame = controlEndgame(currPlayer, endgame);

                            System.err.println("disconnecting after drawing");
                                controller.disconnect(currPlayer.getNickname());
                                if(board.getGamePhase()==GamePhase.SHOWWIN)
                                    break;
                        } else {
                            isLastTurn = controlIsLastTurn(currPlayer, endgame);
                            //if this is the last turn of the last round
                            if (isLastTurn)
                                break;

                            controlPostDraw();

                            System.err.println("finished turn " + ((i - 1) % numOfPlayers + 1) + " of the set n° " + (i - 1) / numOfPlayers);
                        }
                    }
                }
            }
            endgame = controlEndgame(currPlayer, endgame);
            System.err.println(">>>>>>>>>>>>>>>>>CHECK END GAME IS " + board.checkEndgame() + "<<<<<<<<<<<<<<<<<");

            i++;
        }

        // assertEquals(lastPlayer, getLastActivePlayer());
        System.err.println("last player is " + lastPlayer.getNickname());
        System.err.println("finito al turno " + i);
        assertTrue(endgame, "endgame may be false");
        assertTrue(isLastTurn, "isLastTurn is " + isLastTurn);
        assertEquals(GamePhase.SHOWWIN, board.getGamePhase());
        assertEquals(EndgameState.class, controller.getGameState().getClass());
    }


    private void joinOrDisconnect(Player djPlayer){
        if (djPlayer.isConnected()) {
            controller.disconnect(djPlayer.getNickname());
            System.err.println(djPlayer +" disconnected");
        }
        else{
            controller.join(djPlayer.getNickname(), new PuppetClient());
            System.err.println(djPlayer + " joined");
        }
    }
    private boolean isDjBreakPhase(){
        return board.getGamePhase() == GamePhase.SHOWWIN
                || board.getGamePhase() == GamePhase.WAITING_FOR_REJOIN;
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3, 4})
//    @RepeatedTest(500)
    public void simulateRandomGameWithDisconnectAndJoin(int numOfPlayers){
//        int numOfPlayers = 2;
        setUp(numOfPlayers);

        assertEquals(GamePhase.PLACECARD, board.getGamePhase());
        assertEquals(PlayState.class, controller.getGameState().getClass());

        Player djPlayer=board.getPlayerAreas().keySet().stream().toList().get(new Random().nextInt(board.getPlayerAreas().size()));
        final int djTurn=new Random().nextInt(16);
        int djPhase=-1;
        int i = 1;
        Player currPlayer;

        while (!isDjBreakPhase()) {
            if ((i % 16 == djTurn && djPhase == 0) && new Random().nextBoolean()) {
                joinOrDisconnect(djPlayer);
                if (isDjBreakPhase())
                    break;
            }

            currPlayer = board.getCurrentPlayer();
            if (currPlayer.getHand().isHandFull() || !board.canDraw()){
                //disconnect part
                assertTrue(currPlayer.isConnected(), "CURR_PLAYER NOT CONNECTED");

                placeRandomCard(currPlayer);
                if ((i == djTurn && djPhase == 1) && new Random().nextBoolean()) {
                    joinOrDisconnect(djPlayer);
                }
                if(isDjBreakPhase())
                    break;
                // check is useful in case djPlayer was current player and he just disconnected
                if (currPlayer.isConnected()) {

                    //if I can't draw
                    if (!board.canDraw()) {
                        if(isDjBreakPhase())
                            break;
                        controlPostPlaceCantDraw();
                    } else { //if I can still draw
                        controlPostPlaceDrawable();
                        drawRandomCard(currPlayer);
                        if(isDjBreakPhase())
                            break;

                        if ((i % 16 == djTurn && djPhase == 2) && new Random().nextBoolean()) {
                            joinOrDisconnect(djPlayer);
                            if(isDjBreakPhase())
                                break;
                        }
                        controlPostDraw();
                    }
                }
            }
            else{
                assertFalse(currPlayer.getHand().isHandFull());
                controlPostPlaceDrawable();
                drawRandomCard(currPlayer);
                assertTrue(currPlayer.getHand().isHandFull());

                if ((i % 16 == djTurn && djPhase == 2) && new Random().nextBoolean()) {
                    joinOrDisconnect(djPlayer);
                    if (isDjBreakPhase())
                        break;
                }
                controlPostDraw();
            }

            if (i % 16 == djTurn)
                djPhase = new Random().nextInt(3);

            i++;
        }

        System.err.println("lastTurn is "+i);

        assertTrue(board.getNumOfConnectedPlayers() > 0, "Players shouldn't be able to all disconnect");
        if(board.isOnePlayerRemaining()){
            onePlayerRemainingLastChecks();
        }
        else{ //it should be SHOW WINNERS phase
            System.err.println("which players are inside the game?" + board.getPlayerAreas().keySet());
            assertEquals(GamePhase.SHOWWIN, board.getGamePhase(), "Game ended but not in SHOW WINNERS?");
            assertEquals(EndgameState.class, controller.getGameState().getClass());
        }
    }

    //@ParameterizedTest
    //@ValueSource(ints = {2, 3, 4})
    @Test
    public void simulateRandomGameWithEverybodyDisconnecting(/*int numOfPlayers*/){
        int numOfPlayers=4;
        setUp(/*4*/numOfPlayers);
        assertEquals(GamePhase.PLACECARD, board.getGamePhase());
        assertEquals(PlayState.class, controller.getGameState().getClass());

        List<Integer> discTurns=new ArrayList<>();
        for(int j=0; j<numOfPlayers; j++)
            discTurns.add(new Random().nextInt(15) + (discTurns.isEmpty() ? 0 : discTurns.get(discTurns.size()-1))+1);
        System.out.println(discTurns);
        int i = 1;
        int whereToDisconnect=-1;

        while (board.getNumOfConnectedPlayers() >= 2) {
            //setup test single turn
            if(discTurns.contains(i))
                whereToDisconnect=new Random().nextInt(3);
            Player currPlayer = board.getCurrentPlayer();

            if(discTurns.contains(i) && whereToDisconnect==0)
                controller.disconnect(currPlayer.getNickname());
            else {
                placeRandomCard(currPlayer);
                if (board.canDraw()){
                    controlPostPlaceDrawable();
                    if(discTurns.contains(i) && whereToDisconnect==1)
                        controller.disconnect(currPlayer.getNickname());
                    else{
                        drawRandomCard(currPlayer);
                        if(board.getGamePhase() == GamePhase.SHOWWIN){
                            break;
                        }
                        controlPostDraw();
                        if(discTurns.contains(i) && whereToDisconnect==2)
                            controller.disconnect(currPlayer.getNickname());
                    }
                }
                else{
                    controlPostPlaceCantDraw();
                    if(discTurns.contains(i) && whereToDisconnect==2)
                        controller.disconnect(currPlayer.getNickname());
                }
            }
            i++;
        }
        //now one player is remaning
        assertTrue(board.isOnePlayerRemaining());
        onePlayerRemainingLastChecks();
    }
    private void onePlayerRemainingLastChecks(){
        assertEquals(GamePhase.WAITING_FOR_REJOIN, board.getGamePhase(), "WRONG PHASE ON ONE CONNECTED PLAYER");

        Player remainingPlayer = board.getPlayerAreas().keySet().stream()
                .filter(Player::isConnected).findFirst().orElseThrow(); //always one remaining
        // turn doesn't advance in WAITING_FOR_REJOIN phase, so I needed to find the last player

        controller.disconnect(remainingPlayer.getNickname());
        assertNotSame(board, controller.getGameState().board);
        board = controller.getGameState().board;
        assertEquals(CreationState.class, controller.getGameState().getClass());
        assertEquals(GamePhase.CREATE, board.getGamePhase());
    }

    @Test
    void doubleJoin(){
        setUp(2);
        controller.disconnect(playerNickname);
        controller.join(playerNickname, new PuppetClient());
        assertThrows(
                IllegalStateException.class,
                () -> controller.join(playerNickname, new PuppetClient())
        );
    }
    @Test
    void doubleDisconnect(){
        setUp(2);
        controller.disconnect(playerNickname);
        assertThrows(
                IllegalArgumentException.class,
                () -> controller.disconnect("Player 5")
        );
    }
}
