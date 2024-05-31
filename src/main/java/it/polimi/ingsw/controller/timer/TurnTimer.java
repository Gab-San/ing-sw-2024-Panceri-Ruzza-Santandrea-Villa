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
    private Future<?> task;

    public TurnTimer(Player player, int turnTime){
        this.player = player;
        this.turnTime = turnTime;
    }

    @Override
    public void run() {
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
                    System.out.println("Pinging... " + player.getNickname());
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
            System.err.println("TIMER INTERRUPTED");
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
