package it.polimi.ingsw.network;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.Point;
import it.polimi.ingsw.network.rmi.RMIServer;
import it.polimi.ingsw.stub.PuppetClient;
import it.polimi.ingsw.view.ViewCardGenerator;
import it.polimi.ingsw.view.model.ViewBoard;
import it.polimi.ingsw.view.model.ViewPlayArea;
import it.polimi.ingsw.view.model.cards.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {
    private Parser parser;
    private ViewBoard board;
    @BeforeEach
    void setup() {
        board = new ViewBoard("Test_Player");
        parser = new Parser(new PuppetClient(), board);
    }

    //TODO: Fix this test according with the checks on ViewBoard

    @Test
    void patternTest(){
        assertAll(
                () -> assertTrue(Pattern.matches("[2-4]", "2")),
                () -> assertFalse(Pattern.matches("[2-4]", "0")),
                () -> assertFalse(Pattern.matches("2-4", "2")),
                () -> assertTrue(Pattern.matches("[RGrg][0-2]", "R2")),
                () -> assertTrue(Pattern.matches("[RGrg][0-2]", "G2")),
                () -> assertTrue(Pattern.matches("[RGrg][0-2]", "R0")),
                () -> assertTrue(Pattern.matches("[RGrg][0-2]", "r1")),
                () -> assertTrue(Pattern.matches("[RGrg][0-2]", "g0")),
                () -> assertFalse(Pattern.matches("[RGrg][0-2]", "0")),
                () -> assertFalse(Pattern.matches("[RGrg][0-2]", "G")),
                () -> assertFalse(Pattern.matches("[RGrg][0-2]", "2g")),
                () -> assertTrue(Pattern.matches("\\b[RGrg][1-3]?[0-9]\\b", "R31")),
                () -> assertTrue(Pattern.matches("\\b[RGrg][1-3]?[0-9]\\b", "r29")),
                () -> assertTrue(Pattern.matches("\\b[RGrg][1-3]?[0-9]\\b", "R15")),
                () -> assertTrue(Pattern.matches("\\b[RGrg][1-3]?[0-9]\\b", "R5")),
                () -> assertTrue(Pattern.matches("\\b[RGrg][1-3]?[0-9]\\b", "r1")),
                () -> assertTrue(Pattern.matches("\\b[RGrg][1-3]?[0-9]\\b", "g0")),
                () -> assertTrue(Pattern.matches("\\b[RGrg][1-3]?[0-9]\\b", "G9")),
                () -> assertFalse(Pattern.matches("\\b[RGrg][1-3]?[0-9]\\b", "R41")),
                () -> assertFalse(Pattern.matches("\\b[RGrg][1-3]?[0-9]\\b", "r40")),
                () -> assertFalse(Pattern.matches("\\b[RGrg][1-3]?[0-9]\\b", "G49")),
                () -> assertFalse(Pattern.matches("\\b[RGrg][1-3]?[0-9]\\b", "g")),
                () -> assertFalse(Pattern.matches("\\b[RGrg][1-3]?[0-9]\\b", "R100")),
                () -> assertFalse(Pattern.matches("\\b[RGrg][1-3]?[0-9]\\b", "R528")),
                () -> assertFalse(Pattern.matches("\\b[RGrg][1-3]?[0-9]\\b", "R369")),
                () -> assertTrue(Pattern.matches("[Bb]lue|[Rr]ed|[Yy]ellow|[Gg]reen","Blue")),
                () -> assertTrue(Pattern.matches("[Bb]lue|[Rr]ed|[Yy]ellow|[Gg]reen","Green")),
                () -> assertTrue(Pattern.matches("[Bb]lue|[Rr]ed|[Yy]ellow|[Gg]reen","yellow")),
                () -> assertTrue(Pattern.matches("[Bb]lue|[Rr]ed|[Yy]ellow|[Gg]reen","red")),
                () -> assertFalse(Pattern.matches("[Bb]lue|[Rr]ed|[Yy]ellow|[Gg]reen","R"))
        );
    }

    @Test
    void patternGroupTest(){
        Matcher matcher = Pattern.compile("[RGrg][0-2]").matcher("place g0 on g1 TL");

        MatchResult result = matcher.toMatchResult();
        System.out.println("Current matcher: " + result);

        if(matcher.find()) {
            System.out.println("Match Found!!");
        }
        assertEquals("g0",
                matcher.group()
        );
    }


    @Test
    void parseTestCmd() throws RemoteException {
        parser.parseCommand("set num of players to 2");
        assertThrows(
                IllegalArgumentException.class,
                () -> parser.parseCommand("num of players to 2")
        );

        parser.parseCommand("set 2");
        parser.parseCommand("set 4");
        parser.parseCommand("set 3 players");
        assertThrows(
                IllegalArgumentException.class,
                () -> parser.parseCommand("set num 1")
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> parser.parseCommand("set num of players")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> parser.parseCommand("set num of players 6")
        );
    }

    @Test
    void parseSendCmd() throws RemoteException {
        parser.parseCommand("send \"all\" CIAO CIAO MAMMINA");
    }

    @Test
    void parseDisconnectCmd() throws RemoteException {
        parser.parseCommand("disconnect");
        parser.parseCommand("disconnect me");
        parser.parseCommand("disconnect now");
    }


    @Test
    void parseConnectCmd() throws RemoteException {
        parser.parseCommand("connect Johnny Sins");
        assertThrows(
                IllegalArgumentException.class,
                () -> parser.parseCommand("connect")
        );
        parser.parseCommand("connect Gianni Morandi quel grandissimo uomo che c'ha na fetta di grana in una mano e una fetta di caviale nell'altra");
    }


    @Test
    void parseDrawCommand() throws RemoteException {
        assertThrows(
                IllegalArgumentException.class,
                () -> parser.parseCommand("draw")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> parser.parseCommand("draw R")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> parser.parseCommand("draw H 2")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> parser.parseCommand("draw R 3")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> parser.parseCommand("draw rg")
        );

        parser.parseCommand("draw R2");
        parser.parseCommand("draw g0");

        try{
            parser.parseCommand("draw g 1");
        } catch (IllegalArgumentException exc){
            System.err.println(exc.getMessage());
        }
        try{
            parser.parseCommand("draw h2");
        } catch (IllegalArgumentException exc){
            System.err.println(exc.getMessage());
        }
        try{
            parser.parseCommand("draw g4");
        } catch (IllegalArgumentException exc){
            System.err.println(exc.getMessage());
        }


    }

    @Test
    void parseReconnectCmd() throws RemoteException {
        assertThrows(
                IllegalArgumentException.class,
                () -> parser.parseCommand(" RECONNECT   ")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> parser.parseCommand("RECONNECT   TCP ")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> parser.parseCommand("RECONNECT TCP www.gg 4561")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> parser.parseCommand("RECONNECT rmi www.gg 4561")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> parser.parseCommand("reconnect FLING localhost 484848")
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> parser.parseCommand("reconnect TCP localhost sus")
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> parser.parseCommand("reconnect TCP kek sus")
        );
        new RMIServer(1564);
        parser.parseCommand("reconnect RMI localhost 1564");

        parser.parseCommand(" reconnect   RMI   localhost   1564    ");
        parser.parseCommand("connect Gamba");
    }

    @Test
    void parseChooseColorCmd() throws RemoteException {
        parser.parseCommand("choose color Blue");
        parser.parseCommand("choose color blue");
        assertThrows(
                IllegalArgumentException.class,
                ()->parser.parseCommand("choose color Black")
        );
        assertThrows(
                IllegalArgumentException.class,
                ()->parser.parseCommand("choose color lack")
        );
        assertThrows(
                IllegalArgumentException.class,
                ()->parser.parseCommand("choose color lack")
        );
        try{
            parser.parseCommand("choose color Cyan");
        } catch (IllegalArgumentException exc){
            System.err.println(exc.getMessage());
        }
    }

    @Test
    void parseChooseObjectiveCommand() throws RemoteException {
        parser.parseCommand("Choose obj 1");
        parser.parseCommand("Choose objective card 2");
        assertThrows(
                IllegalArgumentException.class,
                () -> parser.parseCommand("Choose 3")
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> parser.parseCommand("Choose sussy baka 4")
        );
        try{
            parser.parseCommand("Choose objective card 3");
        } catch (IllegalArgumentException exception){
            System.err.println(exception.getMessage());
        }

        try {
            parser.parseCommand("choose objective");
        } catch (IllegalArgumentException e){
            System.err.println(e.getMessage());
        }
    }

    @Test
    void parseRestartCmd() throws RemoteException {
        assertThrows(
                IllegalArgumentException.class,
                () ->parser.parseCommand("restart players")
        );
        assertThrows(
                IllegalArgumentException.class,
                () ->parser.parseCommand("restart players 10")
        );
        assertThrows(
                IllegalArgumentException.class,
                () ->parser.parseCommand("restart 10")
        );
        assertThrows(
                IllegalArgumentException.class,
                () ->parser.parseCommand("restart")
        );

        try{
            parser.parseCommand("restart players");
        } catch (IllegalArgumentException exception){
            System.err.println(exception.getMessage());
        }

        try{
            parser.parseCommand("restart 20");
        } catch (IllegalArgumentException exception){
            System.err.println(exception.getMessage());
        }
        try{
            parser.parseCommand("restart");
        } catch (IllegalArgumentException exception){
            System.err.println(exception.getMessage());
        }

        parser.parseCommand("restart with 3 players");
    }


    @Test
    void parsePlaceStartingCard() throws RemoteException {
        assertThrows(
                IllegalArgumentException.class,
                () -> parser.parseCommand("Place starting card S0")
        );
        board.getPlayerHand().setStartCard(ViewCardGenerator.getRandomStartingCard());
        parser.parseCommand("Place starting");
        board.getPlayerHand().setStartCard(ViewCardGenerator.getRandomStartingCard());
        parser.parseCommand("Place starting card");
    }

    @Test
    void parsePlaceCard() throws RemoteException {
        assertThrows(
                IllegalArgumentException.class,
                () -> parser.parseCommand("play G0")
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> parser.parseCommand("play G255 on G29 TL")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> parser.parseCommand("play GO on G3")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> parser.parseCommand("play G0 TL")
        );

        List<ViewCorner> emptyCorners = new LinkedList<>();
        emptyCorners.add(new ViewCorner(null, null, CornerDirection.TR));
        emptyCorners.add(new ViewCorner(null, null, CornerDirection.TL));
        emptyCorners.add(new ViewCorner(null, null, CornerDirection.BR));
        emptyCorners.add(new ViewCorner(null, null, CornerDirection.BL));

        List<ViewCorner> otherCorners = new LinkedList<>();
        emptyCorners.forEach(c->otherCorners.add(new ViewCorner(c)));

        ViewStartCard startCard = new ViewStartCard("S0", "", "",
                emptyCorners, new LinkedList<>());
        ViewPlayArea playArea = board.getPlayerArea(board.getPlayerHand().getNickname());
        playArea.placeStarting(startCard);

        ViewGoldCard cardInHand = ViewCardGenerator.getRandomGoldCard();
        board.getPlayerHand().addCard(cardInHand);

        ViewPlayCard cardInPlay = new ViewResourceCard("R21", "", "",
                otherCorners, 1, GameResource.LEAF);
        playArea.placeCard(new Point(1,1) , cardInPlay);

        assertEquals("R21", playArea.getCardAt(new Point(1,1)).getCardID());

        System.out.println("Placed card ID: " + cardInPlay.getCardID());
        parser.parseCommand("play "+ cardInHand.getCardID() +" on "+ cardInPlay.getCardID() +" TR");
    }

}