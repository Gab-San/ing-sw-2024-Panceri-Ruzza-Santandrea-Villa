package it.polimi.ingsw.controller.timer;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.ListenException;
import it.polimi.ingsw.model.listener.remote.errors.PingEvent;
import it.polimi.ingsw.model.listener.remote.errors.IndirectDisconnectEvent;
import it.polimi.ingsw.network.CentralServer;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

/**
 * A timer that handles a player's turn duration, sending periodic pings to the player
 * to keep the connection alive and disconnecting the player if they exceed their turn time.
 */
public class TurnTimer implements Runnable{

    private final Player player;
    private final int turnTime;
    private final long pingTimeMillis;
    private Future<?> task;

    /**
     * Constructs a TurnTimer object with the specified player, turn time, and ping time.
     * @param player The player associated with this timer.
     * @param turnTime The duration of the player's turn in seconds.
     * @param pingTime The interval between ping messages in seconds.
     */
    public TurnTimer(Player player, int turnTime, int pingTime){
        this.player = player;
        this.turnTime = turnTime;
        this.pingTimeMillis = pingTime*1000L;
    }

    /**
     * Runs the TurnTimer, managing the player's turn duration, sending periodic pings,
     * and disconnecting the player if they exceed their turn time.
     */
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
//                    System.out.println("PINGING...");
                    player.notifyAllListeners(new PingEvent(player.getNickname()));
                } catch (ListenException connectionException) {
                    System.out.println("CANNOT PING " + player.getNickname());
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

    /**
     * Sets the task associated with this timer.
     * @param task The task to set.
     */
    void setTask(Future<?> task){
        this.task = task;
    }

    /**
     * Cancels the task associated with this timer if it exists.
     */
    void killTask(){
        if(task != null){
            task.cancel(true);
        }
    }
}
