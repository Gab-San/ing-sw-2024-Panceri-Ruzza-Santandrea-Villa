package it.polimi.ingsw.controller;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.model.listener.remote.errors.IllegalActionError;
import it.polimi.ingsw.network.VirtualClient;

import java.util.List;

public class JoinState extends GameState {
    int numOfPlayersToStart;

    public JoinState(Board board, BoardController controller, List<String> disconnectingPlayers, int num){
        super(board, controller, disconnectingPlayers);
        numOfPlayersToStart = num;
        board.setGamePhase(GamePhase.JOIN);
    }
    @Override
    public void join(String nickname, VirtualClient client) throws IllegalStateException {
        if(board.getGamePhase()!=GamePhase.JOIN)
            throw new IllegalStateException("IMPOSSIBLE TO JOIN IN THIS PHASE");
        board.addPlayer(new Player(nickname));
        board.subscribeClientToUpdates(nickname, client);
        if(!disconnectingPlayers.isEmpty()) return;
        if(board.getPlayerAreas().size() == numOfPlayersToStart) {
            transition(new SetupState(board, controller, disconnectingPlayers));
        }
    }

    @Override
    public void disconnect(String nickname) throws IllegalStateException, IllegalArgumentException {
        board.unsubscribeClientFromUpdates(nickname);
        board.removePlayer(nickname);
    }

    @Override
    public void setNumOfPlayers(String nickname, int num) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO CHANGE THE NUMBER OF PLAYERS DURING JOIN STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO CHANGE THE NUMBER OF PLAYERS DURING JOIN STATE");
    }

    @Override
    public void placeStartingCard(String nickname, boolean placeOnFront) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO PLACE STARTING CARDS DURING JOIN STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO PLACE STARTING CARDS DURING JOIN STATE");
    }
    public void chooseYourColor(String nickname, PlayerColor color) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO CHOOSE YOUR COLOR DURING CREATION STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO CHOOSE YOUR COLOR DURING CREATION STATE");
    }

    @Override
    public void chooseSecretObjective(String nickname, int choice) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO CHOOSE SECRETE OBJECTIVE DURING JOIN STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO CHOOSE SECRETE OBJECTIVE DURING JOIN STATE");
    }

    @Override
    public void draw(String nickname, char deckFrom, int cardPos) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname,"IMPOSSIBLE TO DRAW STARTING CARDS DURING JOIN STATE" ));
        throw new IllegalStateException("IMPOSSIBLE TO DRAW STARTING CARDS DURING JOIN STATE");
    }

    @Override
    public void placeCard(String nickname, String cardID, Point cardPos, CornerDirection cornerDir, boolean placeOnFront) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO PLACE CARDS DURING JOIN STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO PLACE CARDS DURING JOIN STATE");
    }

    @Override
    public void restartGame(String nickname, int numOfPlayers) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname,"IMPOSSIBLE TO START A NEW GAME DURING JOIN STATE" ));
        throw new IllegalStateException("IMPOSSIBLE TO START A NEW GAME DURING JOIN STATE");
    }
}
