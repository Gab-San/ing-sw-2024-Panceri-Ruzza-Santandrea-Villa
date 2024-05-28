package it.polimi.ingsw.controller.timer;

import it.polimi.ingsw.stub.PuppetClient;
import it.polimi.ingsw.stub.PuppetController2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TurnTimerControllerTest {

    @Test
    void timerTest() throws InterruptedException {
        PuppetController2 controller = new PuppetController2();
        controller.join("Gamba", new PuppetClient());
        Thread.sleep(1000);
        controller.join("Fibonacci", new PuppetClient());
        while(controller.contains("Fibonacci"));
    }

    @Test
    void timerTest2() throws InterruptedException {
        PuppetController2 controller = new PuppetController2();
        controller.join("Gamba", new PuppetClient());
        Thread.sleep(5000);
        while(controller.contains("Fibonacci"));
    }

    @Test
    void timerTestStop() throws InterruptedException {
        PuppetController2 controller = new PuppetController2();
        controller.join("Giacomo", new PuppetClient());
        Thread.sleep(1000);
        controller.interruptTimer("Giacomo");
        assertTrue(controller.contains("Giacomo"));
    }


    @Test
    void timerTestAll() throws InterruptedException {
        PuppetController2 controller = new PuppetController2();
        controller.singleJoin("Giacomo", new PuppetClient());
        controller.singleJoin("FRIZZI", new PuppetClient());
        controller.singleJoin("Paolino", new PuppetClient());
        Thread.sleep(2000);
        controller.startAllTimers();
        while (controller.contains("Paolino"));
    }

    @Test
    void timerTestStopAll() throws InterruptedException {
        PuppetController2 controller = new PuppetController2();
        controller.singleJoin("Giacomo", new PuppetClient());
        controller.singleJoin("FRIZZI", new PuppetClient());
        controller.singleJoin("Paolino", new PuppetClient());
        Thread.sleep(2000);
        controller.startAllTimers();
        Thread.sleep(3000);
        controller.stopAllTimers();
        assertTrue(controller.contains("Paolino"));
        assertTrue(controller.contains("FRIZZI"));
        assertTrue(controller.contains("Giacomo"));
    }

}