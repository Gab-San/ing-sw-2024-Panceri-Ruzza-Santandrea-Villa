package it.polimi.ingsw.controller.timer;

import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public synchronized void startTimer(Player currPlayer, int turnTimeSeconds){
        TurnTimer timer = new TurnTimer(currPlayer, turnTimeSeconds);
        Future<?> timerFuture = timersPool.submit(timer);
        timer.setTask(timerFuture);
        timers.put(currPlayer, timer);
    }

    /**
     * Stops the player timer if running
     * @param currPlayer the player whose turn has finished
     */
    public synchronized void stopTimer(Player currPlayer){
        System.out.println("STOPPING TIMER FOR: " + currPlayer.getNickname());
        TurnTimer currentTimer = timers.get(currPlayer);
        if(currentTimer != null) {
            currentTimer.killTask();
            timers.remove(currPlayer);
        }
    }



    public synchronized void startAll(List<Player> playerList, int turnTime){
        for(Player player: playerList){
            startTimer(player, turnTime);
        }
    }

    public synchronized void stopAll(){
        List<Player> stoppingTimers = new ArrayList<>(timers.keySet());

        for(Player player: stoppingTimers){
            stopTimer(player);
        }
    }
}
