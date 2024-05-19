package it.polimi.ingsw.model.listener.remote.events;


import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.PlayerColor;
import it.polimi.ingsw.model.listener.remote.events.stub.StubClient;
import it.polimi.ingsw.model.listener.remote.events.stub.StubView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

class EventTest {
    private StubClient client;
    private Board board;
    private final String nick= "Gamba";

    @BeforeEach
    void setup(){
        StubView view = new StubView();
        client = new StubClient(nick, view);
        board = new Board("SUSSY BAKA");
        board.subscribeClientToUpdates(nick, client);
    }

    @AfterEach
    void close(){
        board.unsubscribeClientFromUpdates(nick);
    }

    @Test
    void testCreatePlayer() throws InterruptedException {
        board.addPlayer(new Player(nick));
//        Thread.sleep(1000);
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
    }

    @Test
    void testPlayerUpdates() throws InterruptedException {
        Player player = new Player(nick);
        board.addPlayer(player);
        player.setConnected(false);
        player.setTurn(2);
        player.setColor(PlayerColor.BLUE);
        board.setPlayerDeadLock(player, true);
//        Thread.sleep(1000);
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
    }


    @Test
    void testBoardEvents() throws InterruptedException {
        board.setGamePhase(GamePhase.DRAWCARD);
        board.setCurrentTurn(3);
        board.nextTurn();
//        Thread.sleep(1000);
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
    }

    @Test
    void testObjectiveReveal() throws InterruptedException {
        board.revealObjectives();
//        Thread.sleep(1000);
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
    }
}