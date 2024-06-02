package it.polimi.ingsw.controller.timer;

import it.polimi.ingsw.model.Player;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * The TurnTimerController delivers an interface that can be used to control the timers of
 * a turn. Exposing only the start and stop methods this class lets the user set up timers
 * for an entire turn without any method to reset when needed.
 */
public class TurnTimerController {
    private final ExecutorService timersPool;
    private static final int DEFAULT_PING_TIME_SECONDS = 20;
    private final Map<Player, TurnTimer> timers;
    public TurnTimerController(){
        timersPool = Executors.newCachedThreadPool();
        timers = new HashMap<>();

    }

    /**
     * Start the timer for the given player
     * @param currPlayer player whose turn is starting
     * @param turnTimeSeconds time of each turn in seconds
     */

    public synchronized void startTimer(Player currPlayer, int turnTimeSeconds, int pingTimeSeconds){
        TurnTimer timer = new TurnTimer(currPlayer, turnTimeSeconds, pingTimeSeconds);
        Future<?> timerFuture = timersPool.submit(timer);
        timer.setTask(timerFuture);
        timers.put(currPlayer, timer);
    }

    public synchronized void startTimer(Player player, int turnTimeSeconds){
        startTimer(player, turnTimeSeconds, DEFAULT_PING_TIME_SECONDS);
    }

    /**
     * Stops the player timer if running
     * @param player the player whose turn has finished
     */
    public synchronized void stopTimer(Player player){
//        System.out.println("STOPPING TIMER FOR: " + currPlayer.getNickname());
        TurnTimer currentTimer = timers.get(player);
        if(currentTimer != null) {
            currentTimer.killTask();
            timers.remove(player);
        }
    }


    public synchronized void startAll(List<Player> playerList, int turnTime){
        startAll(playerList, turnTime, DEFAULT_PING_TIME_SECONDS);
    }

    public synchronized void startAll(List<Player> playerList, int turnTime, int pingTimeSeconds){
        for(Player player: playerList){
            startTimer(player, turnTime, pingTimeSeconds);
        }
    }

    public synchronized void stopAll(){
        List<Player> stoppingTimers = new ArrayList<>(timers.keySet());

        for(Player player: stoppingTimers){
            stopTimer(player);
        }
    }
}
