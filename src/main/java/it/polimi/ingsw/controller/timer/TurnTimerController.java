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
 * The TurnTimerController delivers an interface that can be used to control the timers of
 * a turn. Exposing only the start and stop methods this class lets the user set up timers
 * for an entire turn without any method to reset when needed.
 */
public class TurnTimerController {
    private final ExecutorService timersPool;
    private final BoardController controller;
    private final Map<Player, Future<?>> tasks;
    public TurnTimerController(BoardController controller){
        this.controller = controller;
        timersPool = Executors.newCachedThreadPool();
        tasks = new HashMap<>();
    }

    /**
     * Start the timer for the given player
     * @param currPlayer player whose turn is starting
     * @param turnTimeSeconds time of each turn in seconds
     */
    public synchronized void startTimer(Player currPlayer, int turnTimeSeconds){
        //System.out.println(colorize("Starting Timer for " + currPlayer.getNickname(), Attribute.BRIGHT_BLUE_TEXT()));
        TurnTimer timer = new TurnTimer(controller, currPlayer, turnTimeSeconds);
        Future<?> timerFuture = timersPool.submit(timer);
        tasks.put(currPlayer, timerFuture);
    }

    /**
     * Stops the player timer if running
     * @param currPlayer the player whose turn has finished
     */
    public synchronized void stopTimer(Player currPlayer){
        Future<?> task = tasks.get(currPlayer);
        if(task != null) {
            task.cancel(true);
            tasks.remove(currPlayer);
        }
    }



    public synchronized void startAll(List<Player> playerList, int turnTime){
        for(Player player: playerList){
            startTimer(player, turnTime);
        }
    }

    public synchronized void stopAll(){
        List<Player> stoppingTimers = new ArrayList<>(tasks.keySet());

        for(Player player: stoppingTimers){
            stopTimer(player);
        }
    }
}
