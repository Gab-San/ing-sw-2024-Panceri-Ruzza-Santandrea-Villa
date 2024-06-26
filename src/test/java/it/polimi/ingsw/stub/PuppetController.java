package it.polimi.ingsw.stub;

import it.polimi.ingsw.controller.BoardController;
import it.polimi.ingsw.controller.timer.TurnTimerController;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.VirtualClient;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a simpler and more accessible board controller on which checks can be applied.
 */
public class PuppetController extends BoardController {
    private final Board board;
    private final TurnTimerController turnTimerController;
    private final List<String> connectedNicks;
    public PuppetController() {
        super();
        board = new Board();
        this.turnTimerController = new TurnTimerController();
        connectedNicks = new ArrayList<>();
    }

    @Override
    public synchronized void join(String nickname, VirtualClient client) throws IllegalStateException, IllegalArgumentException {
        System.out.println("PLAYER CONNECTING... " + nickname);
        board.addPlayer(new Player(nickname));
        Player player = board.getPlayerByNickname(nickname);
        synchronized (turnTimerController) {
            turnTimerController.startTimer(player, 5);
        }
        board.subscribeClientToUpdates(nickname, client);
        connectedNicks.add(nickname);
    }

    public  void interruptTimer(String nickname){
        synchronized (turnTimerController) {
            turnTimerController.stopTimer(board.getPlayerByNickname(nickname));
        }
    }


    @Override
    public synchronized void disconnect(String nickname) throws IllegalStateException, IllegalArgumentException {
        board.removePlayer(nickname);
        board.unsubscribeClientFromUpdates(nickname);
        connectedNicks.remove(nickname);
        System.out.println("DISCONNECTING PLAYER... " + nickname);
    }

    public synchronized boolean contains(String nickname){
        return connectedNicks.contains(nickname);
    }

    public synchronized void simpleJoin(String nickname, VirtualClient client){
        System.out.println("PLAYER CONNECTING...");
        board.addPlayer(new Player(nickname));
        board.subscribeClientToUpdates(nickname, client);
        connectedNicks.add(nickname);
    }

    public synchronized void startAllTimers(){
        turnTimerController.startAll(board.getPlayerAreas().keySet().stream().toList(), 5);
    }
    public synchronized void stopAllTimers(){
        turnTimerController.stopAll();
    }
}
