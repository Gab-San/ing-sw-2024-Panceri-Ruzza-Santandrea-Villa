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
import java.util.concurrent.Future;

import static com.diogonunes.jcolor.Ansi.colorize;

public class TurnTimer implements Runnable{

    private final Player player;
    private final int turnTime;
    private final long pingTimeMillis;
    private Future<?> task;

    public TurnTimer(Player player, int turnTime, int pingTime){
        this.player = player;
        this.turnTime = turnTime;
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
                    player.notifyAllListeners(new PingEvent(player.getNickname()));
                } catch (ListenException connectionException) {
                    System.out.println("CANNOT PING " + player.getNickname());
                    player.notifyAllListeners(new IndirectDisconnectEvent(player.getNickname()));
                    timer.cancel();
                    pingTimer.cancel();
                    killTask();
                }
            }
        }, pingTimeMillis, pingTimeMillis);

        try {
            latch.await();
            System.out.println("DISCONNECTING FOR BEING IDLE: " + player.getNickname());
            player.notifyAllListeners(new IndirectDisconnectEvent(player.getNickname()));
        } catch (InterruptedException e) {
//            System.err.println("TIMER INTERRUPTED");
        }finally{
            timer.cancel();
            pingTimer.cancel();
        }
    }

    void setTask(Future<?> task){
        this.task = task;
    }

    void killTask(){
        if(task != null){
            task.cancel(true);
        }
    }
}
