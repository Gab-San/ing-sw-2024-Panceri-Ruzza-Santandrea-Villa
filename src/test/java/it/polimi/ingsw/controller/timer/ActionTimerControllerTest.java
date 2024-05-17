package it.polimi.ingsw.controller.timer;

import it.polimi.ingsw.controller.testclass.PuppetClient;
import it.polimi.ingsw.controller.testclass.PuppetController;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActionTimerControllerTest {

    @Test
    void timerTest() throws InterruptedException {
        PuppetController controller = new PuppetController("Gamba's game");
        controller.join("Gamba", new PuppetClient());
        Thread.sleep(1000);
        controller.join("Fibonacci", new PuppetClient());
        while(controller.contains("Fibonacci"));
    }

    @Test
    void timerTest2() throws InterruptedException {
        PuppetController controller = new PuppetController("Gamba's game");
        controller.singleJoin("Gamba", new PuppetClient());
        Thread.sleep(1000);
        controller.disconnect("Gamba");
        while(controller.contains("Gamba"));
    }
    @Test
    void timerTestStop() throws InterruptedException {
        PuppetController controller = new PuppetController("Gamba's game");
        controller.join("Giacomo", new PuppetClient());
        Thread.sleep(1000);
        controller.interruptTimer("Giacomo");
        assertTrue(controller.contains("Giacomo"));
    }

    @Test
    void timerTestRestart() throws InterruptedException {
        PuppetController controller = new PuppetController("Gamba's game");
        controller.join("Giacomo", new PuppetClient());
        Thread.sleep(2000);
        controller.resetTimer("Giacomo");
        while (controller.contains("Giacomo"));
    }

    @Test
    void timerTestAll() throws InterruptedException {
        PuppetController controller = new PuppetController("Gamba's game");
        controller.singleJoin("Giacomo", new PuppetClient());
        controller.singleJoin("FRIZZI", new PuppetClient());
        controller.singleJoin("Paolino", new PuppetClient());
        Thread.sleep(2000);
        controller.startAllTimers();
        while (controller.contains("Paolino"));
    }

    @Test
    void timerTestStopAll() throws InterruptedException {
        PuppetController controller = new PuppetController("Gamba's game");
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

    @Test
    void timerTestResetAll() throws InterruptedException {
        PuppetController controller = new PuppetController("Gamba's game");
        controller.singleJoin("Giacomo", new PuppetClient());
        controller.singleJoin("FRIZZI", new PuppetClient());
        controller.singleJoin("Paolino", new PuppetClient());
        Thread.sleep(2000);
        controller.startAllTimers();
        Thread.sleep(3000);
        controller.resetAllTimers();
        while (controller.contains("Paolino"));
    }

}