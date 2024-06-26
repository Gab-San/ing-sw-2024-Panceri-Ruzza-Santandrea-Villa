package it.polimi.ingsw.controller.timer;

import it.polimi.ingsw.stub.PuppetClient;
import it.polimi.ingsw.stub.PuppetController;
import org.junit.jupiter.api.Test;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests turn controller.
 */
class TurnTimerControllerTest {

    // Countdown time is tested arbitrarily since the disconnection process cannot be
    // synchronized. A value higher than the timer time is set in order for the execution to be completed

    @Test
    void timerTest() throws InterruptedException {
        PuppetController controller = new PuppetController();
        controller.join("Gamba", new PuppetClient("Gamba", controller));
        PuppetClient puppetClient = new PuppetClient("Fibonacci", controller);
        controller.join("Fibonacci", puppetClient);
        CountDownLatch latch = new CountDownLatch(6);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                latch.countDown();
            }
        }, 1000, 1000);
        latch.await();
        timer.cancel();
        assertTrue(puppetClient.disconnected);
        assertFalse(controller.contains("Fibonacci"));
        assertFalse(controller.contains("Gamba"));
    }

    @Test
    void timerTest2() throws InterruptedException {
        PuppetController controller = new PuppetController();
        PuppetClient puppetClient = new PuppetClient("Gamba", controller);
        controller.join("Gamba", puppetClient);
        CountDownLatch latch = new CountDownLatch(6);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                latch.countDown();
            }
        }, 1000, 1000);
        latch.await();
        timer.cancel();
        assertTrue(puppetClient.disconnected);
        assertFalse(controller.contains("Gamba"));
    }

    @Test
    void timerTestStop() throws InterruptedException {
        PuppetController controller = new PuppetController();
        PuppetClient puppetClient = new PuppetClient("Giacomo", controller);
        controller.join("Giacomo", puppetClient);
        Thread.sleep(1000);
        controller.interruptTimer("Giacomo");
        assertFalse(puppetClient.disconnected);
        assertTrue(controller.contains("Giacomo"));
    }


    @Test
    void timerTestAll() throws InterruptedException {
        PuppetController controller = new PuppetController();
        controller.simpleJoin("Giacomo", new PuppetClient("Giacomo", controller));
        controller.simpleJoin("FRIZZI", new PuppetClient("FRIZZI", controller));
        PuppetClient puppetClient = new PuppetClient("Paolino", controller);
        controller.simpleJoin("Paolino", puppetClient);
        Thread.sleep(2000);
        controller.startAllTimers();
        CountDownLatch latch = new CountDownLatch(6);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                latch.countDown();
            }
        }, 1000, 1000);
        latch.await();
        timer.cancel();
        assertTrue(puppetClient.disconnected);
        assertFalse(controller.contains("Giacomo"));
        assertFalse(controller.contains("FRIZZI"));
        assertFalse(controller.contains("Paolino"));
    }

    @Test
    void timerTestStopAll() throws InterruptedException {
        PuppetController controller = new PuppetController();
        PuppetClient puppetClient = new PuppetClient("Giacomo", controller);
        controller.simpleJoin("Giacomo", puppetClient);
        PuppetClient puppetClient1 = new PuppetClient("FRIZZI", controller);
        controller.simpleJoin("FRIZZI", puppetClient1);
        PuppetClient puppetClient2 = new PuppetClient("Paolino", controller);
        controller.simpleJoin("Paolino", puppetClient2);
        Thread.sleep(2000);
        controller.startAllTimers();
        Thread.sleep(3000);
        controller.stopAllTimers();
        assertTrue(controller.contains("Paolino"));
        assertTrue(controller.contains("FRIZZI"));
        assertTrue(controller.contains("Giacomo"));
    }

}