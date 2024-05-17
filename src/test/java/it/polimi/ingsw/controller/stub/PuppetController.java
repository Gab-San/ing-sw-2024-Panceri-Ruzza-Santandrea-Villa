package it.polimi.ingsw.controller.stub;

import com.diogonunes.jcolor.Attribute;
import it.polimi.ingsw.controller.BoardController;
import it.polimi.ingsw.controller.timer.ActionTimerController;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.DeckInstantiationException;
import it.polimi.ingsw.server.VirtualClient;

import java.util.ArrayList;
import java.util.List;

import static com.diogonunes.jcolor.Ansi.colorize;

public class PuppetController extends BoardController{
    private Board board;
    private final ActionTimerController actionTimerController;
    private List<String> connectedNicks;
    public PuppetController(String gameID) throws DeckInstantiationException {
        super(gameID);
        board = new Board(gameID);
        this.actionTimerController = new ActionTimerController(this);
        connectedNicks = new ArrayList<>();
    }

    @Override
    public synchronized void join(String nickname, VirtualClient client) throws IllegalStateException, IllegalArgumentException {
        System.out.println(colorize("PLAYER CONNECTING...", Attribute.YELLOW_TEXT()));
        board.addPlayer(new Player(nickname));
        Player player = board.getPlayerByNickname(nickname);
        board.subscribeToListeners(player.getNickname(), client);
        actionTimerController.startTimer(player, 5);
        connectedNicks.add(nickname);
    }

    public synchronized void interruptTimer(String nickname){
        actionTimerController.stopTimer(board.getPlayerByNickname(nickname));
    }

    public synchronized void resetTimer(String nickname){
        actionTimerController.resetTimer(board.getPlayerByNickname(nickname));
    }

    @Override
    public synchronized void disconnect(String nickname) throws IllegalStateException, IllegalArgumentException {
        System.out.println(colorize("DISCONNECTING PLAYER...", Attribute.YELLOW_TEXT()));
        actionTimerController.stopTimer(board.getPlayerByNickname(nickname));
        board.removePlayer(nickname);
        board.unsubscribeToListeners(nickname);
        connectedNicks.remove(nickname);
    }

    public synchronized boolean contains(String nickname){
        return connectedNicks.contains(nickname);
    }

    public synchronized void singleJoin(String nickname, VirtualClient client){
        System.out.println(colorize("PLAYER CONNECTING...", Attribute.YELLOW_TEXT()));
        board.addPlayer(new Player(nickname));
        board.subscribeToListeners(nickname,  client);
        connectedNicks.add(nickname);
    }

    public synchronized void startAllTimers(){
        actionTimerController.startAll(board.getPlayerAreas().keySet().stream().toList(), 5);
    }
    public synchronized void stopAllTimers(){
        actionTimerController.stopAll();
    }
    public synchronized void resetAllTimers(){
        actionTimerController.resetAll(board.getPlayerAreas().keySet().stream().toList());
    }
}
