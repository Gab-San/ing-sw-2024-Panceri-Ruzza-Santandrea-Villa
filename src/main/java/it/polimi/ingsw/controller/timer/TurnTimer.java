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
    private final BoardController controller; //FIXME: [Ale] is controller not used?
    private final int turnTime;
    private final long pingTimeMillis;

    public TurnTimer(BoardController controller, Player player, int turnTime, int pingTime){
        this.player = player;
        this.turnTime = turnTime;
        this.controller = controller;
        this.pingTimeMillis = pingTime*1000L;
    }

    @Override
    public void run() {

        CountDownLatch latch = new CountDownLatch(turnTime);
        Timer timer = new Timer(true);
        Timer pingTimer = new Timer(true);

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
//                    System.out.println("TIMER PINGING: " + player.getNickname());
                    player.notifyAllListeners(new PingEvent(player.getNickname()));
                } catch (ListenException connectionException) {
                    player.notifyAllListeners(new IndirectDisconnectEvent(player.getNickname()));
                }
            }
        }, pingTimeMillis, pingTimeMillis);

        try {
            latch.await();
            timer.cancel();
            pingTimer.cancel();
        } catch (InterruptedException e) {
            timer.cancel();
            pingTimer.cancel();
//            System.err.println("TIMER INTERRUPTED " + player.getNickname());
            return;
        }

        player.notifyAllListeners(new IndirectDisconnectEvent(player.getNickname()));
    }

}
