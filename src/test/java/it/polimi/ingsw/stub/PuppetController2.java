package it.polimi.ingsw.stub;


import com.diogonunes.jcolor.Attribute;
import it.polimi.ingsw.controller.BoardController;
import it.polimi.ingsw.controller.timer.TurnTimerController;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.DeckInstantiationException;
import it.polimi.ingsw.network.VirtualClient;

import java.util.ArrayList;
import java.util.List;

import static com.diogonunes.jcolor.Ansi.colorize;

public class PuppetController2 extends BoardController {
    private final Board board;
    private final TurnTimerController turnTimerController;
    private final List<String> connectedNicks;
    public PuppetController2() throws DeckInstantiationException {
        super();
        board = new Board();
        this.turnTimerController = new TurnTimerController(this);
        connectedNicks = new ArrayList<>();
    }

    @Override
    public synchronized void join(String nickname, VirtualClient client) throws IllegalStateException, IllegalArgumentException {
        System.out.println(colorize("PLAYER CONNECTING...", Attribute.YELLOW_TEXT()));
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
        System.out.println(colorize("DISCONNECTING PLAYER...", Attribute.YELLOW_TEXT()));
        board.removePlayer(nickname);
        board.unsubscribeClientFromUpdates(nickname);
        connectedNicks.remove(nickname);
    }

    public synchronized boolean contains(String nickname){
        return connectedNicks.contains(nickname);
    }

    public synchronized void singleJoin(String nickname, VirtualClient client){
        System.out.println(colorize("PLAYER CONNECTING...", Attribute.YELLOW_TEXT()));
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
