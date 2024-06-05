package it.polimi.ingsw.controller;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerHand;
import it.polimi.ingsw.model.exceptions.DeckException;
import it.polimi.ingsw.stub.CountUpdateClient;
import it.polimi.ingsw.stub.StubClient;
import it.polimi.ingsw.stub.StubView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventControllerIntegrationTest {

    StubView view = new StubView();
    StubClient[] clients = new StubClient[4];
    BoardController controller;

    private GamePoint getRandomPoint(){
        Random random = new Random();
        return new GamePoint(random.nextInt(80), random.nextInt(80));
    }

    private CornerDirection getRandomDir(){
        return CornerDirection.values()[new Random().nextInt(4)];
    }


    @BeforeEach
    void setup(){
        controller = new BoardController();
    }


    public void joinUntilEndGameState(){
        String nick1 = "Gamba";
        clients[0] = new StubClient(nick1, new StubView());
        String nick2 = "Alice";
        clients[1] = new StubClient(nick2, new StubView());

        controller.join(nick1, clients[0]);
        controller.setNumOfPlayers(nick1, 2);
        controller.join(nick2, clients[1]);

        //FOR TWO PLAYERS
        controller.placeStartingCard(nick1, false);
        controller.placeStartingCard(nick2, false);
        //FOR THREE PLAYERS
//        controller.placeStartingCard("Player 3", false);
        //FOR FOUR PLAYERS
//        controller.placeStartingCard("Player 4", false);
        //CHOOSING COLOR
        //FOR TWO PLAYERS
        controller.chooseYourColor(nick1, PlayerColor.BLUE);
        controller.chooseYourColor(nick2, PlayerColor.GREEN);
        //FOR THREE PLAYERS
//        controller.chooseYourColor("Player 3", PlayerColor.YELLOW);
        //FOR FOUR PLAYERS
//        controller.chooseYourColor("Player 4", PlayerColor.RED);

        //CHOOSING SECRET OBJ
        //FOR TWO PLAYERS
        controller.chooseSecretObjective(nick1, 1);
        controller.chooseSecretObjective(nick2, 2);
        //FOR THREE PLAYERS
//        controller.chooseSecretObjective("Player 3", 2);
        //FOR FOUR PLAYERS
//        controller.chooseSecretObjective("Player 4", 1);
        assertEquals(PlayState.class, controller.getGameState().getClass());

        Board board = controller.getGameState().board;
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
        // FOURTH TURN
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
        //FIRST TURN
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



    @Test
    void creationStateTest(){
        String nick = "Gamba";
        clients[0] = new StubClient(nick, view);
        controller.join(nick, clients[0]);
        try {
            controller.restartGame(nick, 2);
        } catch (IllegalStateException e){
            assertEquals(clients[0].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }

        try {
            controller.placeCard("Gamba", "S0", getRandomPoint(), getRandomDir(), new Random().nextBoolean());
        } catch (IllegalStateException e){
            assertEquals(clients[0].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }


        try{
            controller.placeStartingCard(nick, true);
        } catch (IllegalStateException e){
            assertEquals(clients[0].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }

        try{
            controller.chooseYourColor(nick, PlayerColor.BLUE);
        } catch (IllegalStateException e){
            assertEquals(clients[0].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }

        try{
            controller.chooseSecretObjective(nick, 1);
        } catch (IllegalStateException e){
            assertEquals(clients[0].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }

        try{
            controller.draw(nick, 'R', 0);
        } catch (IllegalStateException e){
            assertEquals(clients[0].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }

        try{
            controller.setNumOfPlayers(nick, 1);
        } catch (IllegalArgumentException e){
            assertEquals(clients[0].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }

        try{
            controller.setNumOfPlayers(nick, 5);
        } catch (IllegalArgumentException e){
            assertEquals(clients[0].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }

        String notConnectedNick = "JOJO";

        try{
            controller.setNumOfPlayers(notConnectedNick, 2);
        } catch (IllegalArgumentException argumentException){
            CountDownLatch latch = new CountDownLatch(2);

            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    latch.countDown();
                }
            }, 500, 500);

            try {
                latch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            timer.cancel();
            assertEquals(argumentException.getMessage().toUpperCase(), clients[0].reportedError.split(":")[1].trim());
        }
    }

    @Test
    void joinStateTest() {
        String nick = "Gamba";
        clients[0] = new StubClient(nick, view);
        controller.join(nick, clients[0]);
        controller.setNumOfPlayers(nick, 3);
        String nick2 = "Alice";
        clients[1] = new StubClient(nick2, view);
        controller.join(nick2, clients[1]);

        try{
            controller.setNumOfPlayers(nick, 1);
        } catch (IllegalStateException e){
            assertEquals(clients[0].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }

        try{
            controller.placeStartingCard(nick, true);
        } catch (IllegalStateException e){
            assertEquals(clients[0].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }

        try{
            controller.chooseYourColor(nick, PlayerColor.BLUE);
        } catch (IllegalStateException e){
            assertEquals(clients[0].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }

        try{
            controller.chooseSecretObjective(nick, 1);
        } catch (IllegalStateException e){
            assertEquals(clients[0].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }

        try {
            controller.placeCard("Gamba", "S0", getRandomPoint(), getRandomDir(), new Random().nextBoolean());
        } catch (IllegalStateException e){
            assertEquals(clients[0].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }


        try{
            controller.draw(nick, 'R', 0);
        } catch (IllegalStateException e){
            assertEquals(clients[0].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }

        try {
            controller.restartGame(nick, 2);
        } catch (IllegalStateException e){
            assertEquals(clients[0].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }
    }


    @Test
    void setupStateTest() throws InterruptedException {
        String nick1 = "Gamba";
        clients[0] = new StubClient(nick1, view);
        String nick2 = "Alice";
        clients[1] = new StubClient(nick2, view);
        controller.join(nick1, clients[0]);
        controller.setNumOfPlayers(nick1, 2);
        controller.join(nick2, clients[1]);

        CountDownLatch latch = new CountDownLatch(2);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                latch.countDown();
            }
        }, 500, 500);
        latch.await();
        timer.cancel();

        try{
            controller.draw(nick1, 'R', 0);
        } catch (IllegalStateException e){
            assertEquals(clients[0].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }

        try{
            controller.setNumOfPlayers(nick2, 3);
        } catch (IllegalStateException e){
            assertEquals(clients[1].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }

        try{
            controller.restartGame(nick1, 3);
        }catch (IllegalStateException e){
            assertEquals(clients[0].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }

        try{
            controller.placeCard(nick1, "A0", new GamePoint(2, 2), CornerDirection.BL, true);
        }catch (IllegalStateException e){
            assertEquals(clients[0].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }

        controller.placeStartingCard(nick1, true);

        try{
            controller.placeStartingCard(nick1, false);
        } catch (IllegalStateException ignore){}

        try{
            controller.chooseYourColor(nick2, PlayerColor.BLUE);
        } catch (IllegalStateException e){
            assertEquals(clients[1].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }

        try{
            controller.chooseSecretObjective(nick2, 2);
        } catch (IllegalStateException e){
            assertEquals(clients[1].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }

        controller.placeStartingCard(nick2, false);
        controller.chooseYourColor(nick2, PlayerColor.YELLOW);

        try{
            controller.chooseSecretObjective(nick1, 1);
        } catch (IllegalStateException e){
            assertEquals(clients[0].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }

        try {
            controller.chooseYourColor(nick2, PlayerColor.BLUE );
        } catch (IllegalStateException ignore){}

        try{
            controller.chooseYourColor(nick1, PlayerColor.YELLOW);
        } catch (IllegalStateException e){
            assertEquals(clients[0].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }

        controller.chooseYourColor(nick1, PlayerColor.RED);

        CountDownLatch latch2 = new CountDownLatch(5);
        Timer timer2 = new Timer();
        timer2.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                latch2.countDown();
            }
        }, 500, 500);
        latch2.await();
        timer2.cancel();

        controller.chooseSecretObjective(nick1, 1);

        try{
            controller.chooseSecretObjective(nick1, 2);
        } catch (IllegalStateException ignore){}

        controller.chooseSecretObjective(nick2, 1);

        CountDownLatch latch1 = new CountDownLatch(5);
        Timer timer1 = new Timer();
        timer1.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                latch1.countDown();
            }
        }, 500, 500);
        latch1.await();
        timer1.cancel();
    }


    @Test
    void setupStateTest1() throws InterruptedException {
        String nick1 = "Gamba";
        clients[0] = new CountUpdateClient(nick1, view);
        String nick2 = "Alice";
        clients[1] = new StubClient(nick2, view);
        controller.join(nick1, clients[0]);
        controller.setNumOfPlayers(nick1, 2);
        controller.join(nick2, clients[1]);
        controller.disconnect(nick2);
        CountDownLatch latch = new CountDownLatch(2);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                latch.countDown();
            }
        }, 500, 500);
        latch.await();
        timer.cancel();


        controller.placeStartingCard(nick1, true);
        try {
            controller.chooseYourColor(nick1, PlayerColor.RED);
        } catch(RuntimeException ignore){}

        try{
            controller.chooseYourColor(nick1, PlayerColor.BLUE);
        } catch(RuntimeException ignore){}
        CountDownLatch latch2 = new CountDownLatch(5);
        Timer timer2 = new Timer();
        timer2.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                latch2.countDown();
            }
        }, 500, 500);
        latch2.await();
        timer2.cancel();

        controller.chooseSecretObjective(nick1, 1);


        CountDownLatch latch1 = new CountDownLatch(5);
        Timer timer1 = new Timer();
        timer1.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                latch1.countDown();
            }
        }, 500, 500);
        latch1.await();
        timer1.cancel();
    }

    @Test
    void playStateTest() throws InterruptedException {
        String nick1 = "Gamba";
        clients[0] = new StubClient(nick1, view);
        String nick2 = "Alice";
        clients[1] = new StubClient(nick2, view);
        controller.join(nick1, clients[0]);
        controller.setNumOfPlayers(nick1, 2);
        controller.join(nick2, clients[1]);

        CountDownLatch latch = new CountDownLatch(5);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                latch.countDown();
            }
        }, 500, 500);
        latch.await();
        timer.cancel();

        controller.placeStartingCard(nick1, false);
        controller.placeStartingCard(nick2, false);

        controller.chooseYourColor(nick2, PlayerColor.YELLOW);
        controller.chooseYourColor(nick1, PlayerColor.RED);

        CountDownLatch latch2 = new CountDownLatch(5);
        Timer timer2 = new Timer();
        timer2.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                latch2.countDown();
            }
        }, 500, 500);
        latch2.await();
        timer2.cancel();

        controller.chooseSecretObjective(nick1, 1);
        controller.chooseSecretObjective(nick2, 1);

        CountDownLatch latch1 = new CountDownLatch(5);
        Timer timer1 = new Timer();
        timer1.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                latch1.countDown();
            }
        }, 500, 500);
        latch1.await();
        timer1.cancel();

        try{
            controller.setNumOfPlayers(nick1, 2);
        } catch (IllegalStateException e){
            assertEquals(clients[0].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }

        try{
            controller.draw(nick1, 'R', 0);
        } catch (IllegalStateException e){
            assertEquals(clients[0].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }

        try{
            controller.restartGame(nick1, 3);
        }catch (IllegalStateException e){
            assertEquals(clients[0].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }

        try{
            controller.chooseSecretObjective(nick1, 3);
        }catch (IllegalStateException e){
            assertEquals(clients[0].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }

        try{
            controller.chooseYourColor(nick1, PlayerColor.BLUE);
        }catch (IllegalStateException e){
            assertEquals(clients[0].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }

        try{
            controller.placeStartingCard(nick1,true);
        }catch (IllegalStateException e){
            assertEquals(clients[0].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }

        String notConnectedNick = "JOJO";

        try{
            controller.placeCard(notConnectedNick, "A0", new GamePoint(0,0), CornerDirection.BL, false);
        } catch (IllegalArgumentException argumentException){
            CountDownLatch latch4 = new CountDownLatch(2);

            Timer timer4 = new Timer();
            timer4.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    latch4.countDown();
                }
            }, 500, 500);

            try {
                latch4.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            timer4.cancel();
            assertEquals(clients[0].reportedError.split(":")[1].trim(), argumentException.getMessage().toUpperCase());
        }
        boolean hasPlaced = false;
        boolean worthy1 = true;
        boolean worthy2 = true;

        while(!hasPlaced) {
            int cardId2 = new Random().nextInt(40);
            String cardId = "R"+cardId2;
            if(worthy1)
                try {
                    controller.placeCard(nick1, cardId, new GamePoint(0, 0), CornerDirection.BL, false);
                    hasPlaced = true;
                } catch (IllegalStateException e) {
                    assertEquals(clients[0].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
                    worthy1 = false;
                } catch (IllegalArgumentException ignore) {
                }

            if(worthy2)
                try {
                    controller.placeCard(nick2, cardId, new GamePoint(0, 0), CornerDirection.BL, false);
                    hasPlaced = true;
                } catch (IllegalStateException e) {
                    assertEquals(clients[1].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
                    worthy2 = false;
                } catch (IllegalArgumentException ignore) {
                }
        }

        try {
            controller.placeCard(nick1, "", new GamePoint(0, 0), CornerDirection.BL, false);
        } catch (IllegalStateException e) {
            assertEquals(clients[0].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        } catch (IllegalArgumentException ignore) {
        }


        try {
            controller.placeCard(nick2, "", new GamePoint(0, 0), CornerDirection.BL, false);
        } catch (IllegalStateException e) {
            assertEquals(clients[1].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        } catch (IllegalArgumentException ignore) {
        }

        try{
            controller.draw(notConnectedNick, 'R', 2);
        } catch (IllegalArgumentException e){
            CountDownLatch latch4 = new CountDownLatch(2);

            Timer timer4 = new Timer();
            timer4.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    latch4.countDown();
                }
            }, 500, 500);

            try {
                latch4.await();
            } catch (InterruptedException exception) {
                throw new RuntimeException(exception);
            }

            timer4.cancel();
            assertEquals(clients[0].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }

        try{
            controller.draw(nick1, 'R', 0);
        } catch (IllegalStateException e){
            assertEquals(clients[0].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }

        try{
            controller.draw(nick2, 'R', 0);
        } catch (IllegalStateException e){
            assertEquals(clients[1].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }

        hasPlaced = false;
        worthy1 = true;
        worthy2 = true;

        while(!hasPlaced) {
            int cardId2 = new Random().nextInt(40);
            String cardId = "R"+cardId2;
            if(worthy1)
                try {
                    controller.placeCard(nick1, cardId, new GamePoint(0, 0), CornerDirection.TR, false);
                    hasPlaced = true;
                } catch (IllegalStateException e) {
                    assertEquals(clients[0].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
                    worthy1 = false;
                } catch (IllegalArgumentException ignore) {
                }

            if(worthy2)
                try {
                    controller.placeCard(nick2, cardId, new GamePoint(0, 0), CornerDirection.TR, false);
                    hasPlaced = true;
                } catch (IllegalStateException e) {
                    assertEquals(clients[1].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
                    worthy2 = false;
                } catch (IllegalArgumentException ignore) {
                }
        }

        Board board = controller.getGameState().board;
        Player player = new Player("TEST");
        PlayerHand hand = player.getHand();

        while(true){
            try{
                board.drawTop('R',hand);
                hand.popCard(0);
            } catch (DeckException e) {
                break;
            }
        }


        try {
            controller.draw(nick1,'R', 0);
        } catch (IllegalStateException e) {
            assertEquals(clients[0].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        } catch (IllegalArgumentException e) {
            assertEquals(clients[0].reportedError.split(":")[1].trim(), e.getCause().getMessage().toUpperCase());
        }

        try {
            controller.draw(nick2, 'R', 0);
        } catch (IllegalStateException e) {
            assertEquals(clients[1].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        } catch (IllegalArgumentException e) {
            assertEquals(clients[1].reportedError.split(":")[1].trim(), e.getCause().getMessage().toUpperCase());
        }
    }

    @Test
    void endgameStateTest(){
        joinUntilEndGameState();
        String nick1 = "Gamba";
        String nick2 = "Alice";


        try {
            controller.placeCard(nick1, "S0", getRandomPoint(), getRandomDir(), new Random().nextBoolean());
        } catch (IllegalStateException e){
            assertEquals(clients[0].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }


        try{
            controller.placeStartingCard(nick1, true);
        } catch (IllegalStateException e){
            assertEquals(clients[0].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }

        try{
            controller.chooseYourColor(nick2, PlayerColor.BLUE);
        } catch (IllegalStateException e){
            assertEquals(clients[1].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }

        try{
            controller.chooseSecretObjective(nick2, 1);
        } catch (IllegalStateException e){
            assertEquals(clients[1].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }

        try{
            controller.draw(nick1, 'R', 0);
        } catch (IllegalStateException e){
            assertEquals(clients[0].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }

        try{
            controller.setNumOfPlayers(nick1, 2);
        } catch (IllegalStateException e){
            assertEquals(clients[0].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }

        String notConnectedNick = "JOJO";

        try{
            controller.restartGame(notConnectedNick, 3);
        } catch (IllegalArgumentException argumentException){
            CountDownLatch latch = new CountDownLatch(2);

            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    latch.countDown();
                }
            }, 500, 500);

            try {
                latch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            timer.cancel();
            assertEquals(clients[0].reportedError.split(":")[1].trim(), argumentException.getMessage().toUpperCase());
        }

        try{
            controller.restartGame(nick2, 1);
        } catch (IllegalArgumentException e){
            assertEquals(clients[1].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }

        controller.disconnect(nick1);

        try{
            controller.restartGame(nick2, 2);
        } catch (IllegalStateException e){
            assertEquals(clients[0].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }
    }

    @Test
    void joinDisconnectJoinTest() throws InterruptedException {
        controller.join("Gamba", new StubClient("TEST", view ));
        CountDownLatch latch1 = new CountDownLatch(5);
        Timer timer1 = new Timer();
        timer1.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                latch1.countDown();
            }
        }, 500, 500);
        latch1.await();
        timer1.cancel();

        System.err.println("||||||||| Disconnecting player... |||||||||");
        controller.disconnect("Gamba");

        CountDownLatch latch2 = new CountDownLatch(5);
        Timer timer2 = new Timer();
        timer2.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                latch2.countDown();
            }
        }, 500, 500);
        latch2.await();
        timer2.cancel();
        controller.join("Gamba", new StubClient("TEST", view));

        CountDownLatch latch3 = new CountDownLatch(5);
        Timer timer3 = new Timer();
        timer3.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                latch3.countDown();
            }
        }, 500, 500);
        latch3.await();
        timer3.cancel();


        controller.setNumOfPlayers("Gamba", 3);

        controller.join("JOJO", new StubClient("LULZ", view ));
        CountDownLatch latch4 = new CountDownLatch(5);
        Timer timer4 = new Timer();
        timer4.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                latch4.countDown();
            }
        }, 500, 500);
        latch4.await();
        timer4.cancel();

        System.err.println("||||||||| Disconnecting player... |||||||||");
        controller.disconnect("JOJO");

        CountDownLatch latch5 = new CountDownLatch(5);
        Timer timer5 = new Timer();
        timer5.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                latch5.countDown();
            }
        }, 500, 500);
        latch5.await();
        timer5.cancel();
        controller.join("JOJO", new StubClient("LULZ", view));

        CountDownLatch latch6 = new CountDownLatch(5);
        Timer timer6 = new Timer();
        timer6.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                latch6.countDown();
            }
        }, 500, 500);
        latch6.await();
        timer6.cancel();

    }


    @Test
    void joinDisconnectJoinTest2() throws InterruptedException {
        StubClient client = new StubClient("TEST", view);
        controller.join("Gamba", client);

        CountDownLatch latch1 = new CountDownLatch(5);
        Timer timer1 = new Timer();
        timer1.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                latch1.countDown();
            }
        }, 500, 500);
        latch1.await();
        timer1.cancel();

        System.err.println("||||||||| Disconnecting player... |||||||||");
        controller.disconnect("Gamba");

        CountDownLatch latch2 = new CountDownLatch(5);
        Timer timer2 = new Timer();
        timer2.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                latch2.countDown();
            }
        }, 500, 500);
        latch2.await();
        timer2.cancel();

        controller.join("Gamba", client);

        CountDownLatch latch3 = new CountDownLatch(5);
        Timer timer3 = new Timer();
        timer3.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                latch3.countDown();
            }
        }, 500, 500);
        latch3.await();
        timer3.cancel();

    }



}
