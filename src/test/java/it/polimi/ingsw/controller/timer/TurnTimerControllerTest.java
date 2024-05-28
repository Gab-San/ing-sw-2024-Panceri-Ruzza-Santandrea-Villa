package it.polimi.ingsw.controller.timer;

import it.polimi.ingsw.stub.PuppetClient;
import it.polimi.ingsw.stub.PuppetController2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TurnTimerControllerTest {

    @Test
    void timerTest() throws InterruptedException {
        PuppetController2 controller = new PuppetController2();
        controller.join("Gamba", new PuppetClient("Gamba", controller));
        PuppetClient puppetClient = new PuppetClient("Gamba", controller);
        controller.join("Fibonacci", puppetClient);
        while(!puppetClient.disconnected) Thread.sleep(500);
    }

    @Test
    void timerTest2() throws InterruptedException {
        PuppetController2 controller = new PuppetController2();
        PuppetClient puppetClient = new PuppetClient("Gamba", controller);
        controller.join("Gamba", puppetClient);
        Thread.sleep(5000);
        while(!puppetClient.disconnected) Thread.sleep(500);
    }

    @Test
    void timerTestStop() throws InterruptedException {
        PuppetController2 controller = new PuppetController2();
        PuppetClient puppetClient = new PuppetClient("Gamba", controller);
        controller.join("Giacomo", puppetClient);
        Thread.sleep(1000);
        controller.interruptTimer("Giacomo");
        assertFalse(puppetClient.disconnected);
    }


    @Test
    void timerTestAll() throws InterruptedException {
        PuppetController2 controller = new PuppetController2();
        controller.singleJoin("Giacomo", new PuppetClient());
        controller.singleJoin("FRIZZI", new PuppetClient());
        PuppetClient puppetClient = new PuppetClient("Paolino", controller);
        controller.singleJoin("Paolino", puppetClient);
        Thread.sleep(2000);
        controller.startAllTimers();
        while(!puppetClient.disconnected) Thread.sleep(500);
    }

    @Test
    void timerTestStopAll() throws InterruptedException {
        PuppetController2 controller = new PuppetController2();
        PuppetClient puppetClient = new PuppetClient("Giacomo", controller);
        controller.singleJoin("Giacomo", puppetClient);
        PuppetClient puppetClient1 = new PuppetClient("FRIZZI", controller);
        controller.singleJoin("FRIZZI", puppetClient1);
        PuppetClient puppetClient2 = new PuppetClient("Paolino", controller);
        controller.singleJoin("Paolino", puppetClient2);
        Thread.sleep(2000);
        controller.startAllTimers();
        Thread.sleep(3000);
        controller.stopAllTimers();
        assertTrue(controller.contains("Paolino"));
        assertTrue(controller.contains("FRIZZI"));
        assertTrue(controller.contains("Giacomo"));
    }

}