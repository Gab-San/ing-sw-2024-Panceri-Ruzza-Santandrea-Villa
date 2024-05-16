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

public class TimerController {
    private final ExecutorService timersPool;
    private final BoardController controller;
    private final Map<Player, Future<?>> tasks;
    private final Map<Player, TurnTimer> turnTimerMap;
    private final List<Player> stoppedTasks;
    public TimerController(BoardController controller){
        this.controller = controller;
        timersPool = Executors.newCachedThreadPool();
        tasks = new HashMap<>();
        turnTimerMap = new HashMap<>();
        stoppedTasks = new ArrayList<>(4);
        startTaskPurge();
    }

    private void startTaskPurge(){
        timersPool.execute(
                () -> {
                    synchronized (stoppedTasks) {
                        while (stoppedTasks.isEmpty()) {
                            try {
                                stoppedTasks.wait();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        for(Player player: stoppedTasks) {
                            synchronized (turnTimerMap) {
                                turnTimerMap.remove(player);
                            }
                            synchronized (tasks) {
                                tasks.remove(player);
                            }
                        }
                        stoppedTasks.clear();
                    }
                }
        );
    }

    public void startTimer(Player currPlayer, int turnTime){
        System.out.println(colorize("Starting Timer for " + currPlayer.getNickname(), Attribute.BRIGHT_BLUE_TEXT()));
        TurnTimer timer = new TurnTimer(controller, currPlayer, turnTime);
        Future<?> timerFuture = timersPool.submit(timer);
        synchronized (tasks) {
            tasks.put(currPlayer, timerFuture);
        }
        synchronized (turnTimerMap) {
            turnTimerMap.put(currPlayer, timer);
        }
    }

    public void stopTimer(Player currPlayer){
        Future<?> task = tasks.get(currPlayer);
        task.cancel(true);
        synchronized (stoppedTasks) {
            stoppedTasks.add(currPlayer);
            stoppedTasks.notify();
        }
    }

    public void resetTimer(Player currPlayer){
        TurnTimer turnTimer = turnTimerMap.get(currPlayer);
        turnTimer.reset();
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
        for(Player player : tasks.keySet()){
            stopTimer(player);
        }
    }
}
