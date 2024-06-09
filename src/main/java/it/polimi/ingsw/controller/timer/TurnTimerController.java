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
    /**
     * Constructs a TurnTimerController with an associated thread pool and an empty map of timers.
     */
    public TurnTimerController(){
        timersPool = Executors.newCachedThreadPool();
        timers = new HashMap<>();

    }

    /**
     * Starts a timer for the specified player with the given turn time and ping time.
     * <p>
     * This method initializes a new {@link TurnTimer} for the given player and schedules it to run
     * using an ExecutorService. The timer will send ping messages at specified intervals and will
     * keep track of the turn duration.
     * </p>
     * @param currPlayer The player whose turn is starting.
     * @param turnTimeSeconds The duration of the turn in seconds.
     * @param pingTimeSeconds The interval between ping messages in seconds.
     * @throws IllegalArgumentException if the turn time or ping time is non-positive.
     */
    public synchronized void startTimer(Player currPlayer, int turnTimeSeconds, int pingTimeSeconds){
        TurnTimer timer = new TurnTimer(currPlayer, turnTimeSeconds, pingTimeSeconds);
        Future<?> timerFuture = timersPool.submit(timer);
        timer.setTask(timerFuture);
        timers.put(currPlayer, timer);
    }

    /**
     * Starts a timer for the specified player with the given turn time and default ping time.
     * <p>
     * This method behaves similarly to {@link #startTimer(Player, int, int)}, but uses a default ping
     * time of 20 seconds.
     * </p>
     * @param player The player whose turn is starting.
     * @param turnTimeSeconds The duration of the turn in seconds.
     * @throws IllegalArgumentException if the turn time is non-positive.
     */
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

    /**
     * Starts timers for all players in the given list with the specified turn time and default ping time.
     * <p>
     * This method iterates over the list of players and starts a timer for each using the default ping time
     * of 20 seconds.
     * </p>
     * @param playerList The list of players whose turns are starting.
     * @param turnTime The duration of each turn in seconds.
     * @throws IllegalArgumentException if the turn time is non-positive.
     */
    public synchronized void startAll(List<Player> playerList, int turnTime){
        startAll(playerList, turnTime, DEFAULT_PING_TIME_SECONDS);
    }

    /**
     * Starts timers for all players in the given list with the specified turn time and ping time.
     * <p>
     * This method iterates over the list of players and starts a timer for each using the specified turn
     * time and ping time.
     * </p>
     * @param playerList The list of players whose turns are starting.
     * @param turnTime The duration of each turn in seconds.
     * @param pingTimeSeconds The interval between ping messages in seconds.
     * @throws IllegalArgumentException if the turn time or ping time is non-positive.
     */
    public synchronized void startAll(List<Player> playerList, int turnTime, int pingTimeSeconds){
        for(Player player: playerList){
            startTimer(player, turnTime, pingTimeSeconds);
        }
    }

    /**
     * Stops all running timers for players.
     */
    public synchronized void stopAll(){
        List<Player> stoppingTimers = new ArrayList<>(timers.keySet());

        for(Player player: stoppingTimers){
            stopTimer(player);
        }
    }
}
