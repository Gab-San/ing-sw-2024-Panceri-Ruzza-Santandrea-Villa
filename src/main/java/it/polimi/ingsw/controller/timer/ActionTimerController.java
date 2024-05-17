package it.polimi.ingsw.controller.timer;

import com.diogonunes.jcolor.Attribute;
import it.polimi.ingsw.controller.BoardController;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.diogonunes.jcolor.Ansi.colorize;

/**
 * ActionTimerController delivers an interface that lets the user control the timers
 * of an action. Through the use of the reset methods the timers can be restarted.
 */
public class ActionTimerController {
    private final ExecutorService timersPool;
    private final BoardController controller;
    private final Map<Player, Future<?>> tasks;
    private final Map<Player, TurnTimer> turnTimerMap;
    public ActionTimerController(BoardController controller){
        this.controller = controller;
        timersPool = Executors.newCachedThreadPool();
        tasks = new HashMap<>();
        turnTimerMap = new HashMap<>();
    }

    /**
     * Start the timer for the given player
     * @param currPlayer player whose turn is starting
     * @param turnTimeSeconds time of each action in seconds
     */
    public void startTimer(Player currPlayer, int turnTimeSeconds){
        System.out.println(colorize("Starting Timer for " + currPlayer.getNickname(), Attribute.BRIGHT_BLUE_TEXT()));
        TurnTimer timer = new TurnTimer(controller, currPlayer, turnTimeSeconds);
        Future<?> timerFuture = timersPool.submit(timer);
        tasks.put(currPlayer, timerFuture);
        turnTimerMap.put(currPlayer, timer);
    }

    /**
     * Stops the player timer if running
     * @param currPlayer the player whose turn has finished
     */
    public void stopTimer(Player currPlayer){
        Future<?> task = tasks.get(currPlayer);
        if(task != null) {
            task.cancel(true);
            turnTimerMap.remove(currPlayer);
            tasks.remove(currPlayer);
        }
    }

    public void resetTimer(Player currPlayer){
        TurnTimer turnTimer = turnTimerMap.get(currPlayer);
        if(turnTimer != null) {
            turnTimer.reset();
        }
    }

    public void startAll(List<Player> playerList, int turnTime){
        for(Player player: playerList){
            startTimer(player, turnTime);
        }
    }

    public void resetAll(List<Player> playerList){
        for(Player player: playerList){
            resetTimer(player);
        }
    }

    public void stopAll(){
        List<Player> stoppingTimers = new ArrayList<>(tasks.keySet());

        for(Player player: stoppingTimers){
            stopTimer(player);
        }
    }
}
