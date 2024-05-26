package it.polimi.ingsw.network.tcp.message.notifications.board;

import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.tcp.message.TCPServerMessage;

import java.io.Serial;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class BoardStateMessage implements TCPServerMessage {
    @Serial
    private static final long serialVersionUID = 1920384L;
    private final int currentTurn;
    private final Map<String, Integer> scoreboard;
    private final GamePhase gamePhase;
    private final Map<String, Boolean> playerDeadLock;

    public BoardStateMessage(int currentTurn, Map<String, Integer> scoreboard, GamePhase gamePhase, Map<String, Boolean> isPlayerDeadLocked) {
        // TODO: decide if to just use refs
        this.currentTurn = currentTurn;
        this.scoreboard = new HashMap<>();
        for(String nickname: scoreboard.keySet()){
            this.scoreboard.put(nickname, scoreboard.get(nickname));
        }
        this.gamePhase = gamePhase;
        this.playerDeadLock = new HashMap<>();
        for(String nickname : isPlayerDeadLocked.keySet()){
            playerDeadLock.put(nickname, isPlayerDeadLocked.get(nickname));
        }
    }

    @Override
    public boolean isCheck() {
        return false;
    }

    @Override
    public void execute(VirtualClient virtualClient) throws RemoteException {
        virtualClient.setBoardState(currentTurn, scoreboard, gamePhase, playerDeadLock);
    }
}
