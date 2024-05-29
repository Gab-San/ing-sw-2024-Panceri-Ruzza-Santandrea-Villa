package it.polimi.ingsw.controller.timer;

import com.diogonunes.jcolor.Attribute;
import it.polimi.ingsw.controller.BoardController;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.ListenException;
import it.polimi.ingsw.model.listener.remote.errors.PingEvent;
import it.polimi.ingsw.model.listener.remote.errors.IndirectDisconnectEvent;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

import static com.diogonunes.jcolor.Ansi.colorize;

public class TurnTimer implements Runnable{

    private final Player player;
    private final BoardController controller;
    private final int turnTime;

    public TurnTimer(BoardController controller, Player player, int turnTime){
        this.player = player;
        this.turnTime = turnTime;
        this.controller = controller;
    }

    @Override
    public void run() {
        //FIXME: remove colorize
        System.out.println(colorize("STARTING TIMER FOR " + player.getNickname(), Attribute.YELLOW_TEXT()));
        long pingTimeMillis = 20000;

        CountDownLatch latch = new CountDownLatch(turnTime);
        Timer timer = new Timer();
        Timer pingTimer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                latch.countDown();
            }
        }, 1000, 1000);

        pingTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    player.notifyAllListeners(new PingEvent(player.getNickname()));
                } catch (ListenException connectionException) {
                    //FIXME: remove colorize
                    System.out.println(colorize("ENDING TIMER FOR " + player.getNickname(), Attribute.YELLOW_TEXT()));
                    player.notifyAllListeners(new IndirectDisconnectEvent(player.getNickname()));
                }
            }
        }, pingTimeMillis, pingTimeMillis);

        try {
            latch.await();
            timer.cancel();
            pingTimer.cancel();
        } catch (InterruptedException e) {
            System.err.println("TIMER INTERRUPTED");
            return;
        }

        //FIXME: remove colorize
        System.out.println(colorize("ENDING TIMER FOR " + player.getNickname(), Attribute.YELLOW_TEXT()));
        player.notifyAllListeners(new IndirectDisconnectEvent(player.getNickname()));
    }

}
