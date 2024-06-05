package it.polimi.ingsw.controller;

import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.model.listener.remote.errors.IllegalActionError;
import it.polimi.ingsw.model.listener.remote.errors.IllegalParameterError;
import it.polimi.ingsw.network.VirtualClient;

import java.util.List;
import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.PlayerColor;

public class CreationState extends GameState{
    private final int SET_TIME = 60; // 1 minute

    public CreationState(Board board, BoardController controller, List<String> disconnectingPlayers) {
        super(board, controller, disconnectingPlayers);
        board.setGamePhase(GamePhase.CREATE);
    }

    @Override
    public void join(String nickname, VirtualClient client) throws IllegalStateException {
        if(!board.getPlayerAreas().isEmpty()) {
            throw new IllegalStateException("WAITING FOR THE CONNECTED PLAYER TO START THE LOBBY...");
        }
        Player creator = new Player(nickname);
        board.addPlayer(creator);
        board.subscribeClientToUpdates(nickname, client);
        board.setGamePhase(GamePhase.SETNUMPLAYERS);
        timers.startTimer(creator, SET_TIME);
    }

    @Override
    public void disconnect(String nickname) throws IllegalStateException, IllegalArgumentException {
        disconnectingPlayers.remove(nickname);
        board.unsubscribeClientFromUpdates(nickname);
        timers.stopTimer(board.getPlayerByNickname(nickname));
        board.removePlayer(nickname);
        board.setGamePhase(GamePhase.CREATE);
    }

    @Override
    public void setNumOfPlayers(String nickname, int num)
                throws IllegalArgumentException, IllegalStateException{
        if(board.getGamePhase()!=GamePhase.SETNUMPLAYERS) {
            //FIXME: This notification is impossible to reach because if the player has connected then the phase is necessairily the SETNUMPLAYERS
            board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO SET THE NUMBER OF PLAYERS IN THIS PHASE"));
            throw new IllegalStateException("IMPOSSIBLE TO SET THE NUMBER OF PLAYERS IN THIS PHASE");
        }

        Player player = board.getPlayerByNickname(nickname); // throws on player not in game

        if(num<2 || num>4) {
            board.notifyAllListeners(new IllegalParameterError(nickname, "NUMBER OF PLAYERS IN THE GAME MUST BE BETWEEN 2 AND 4 INCLUDED, YOU INSERTED " + num + " PLAYERS" ));
            throw new IllegalArgumentException("NUMBER OF PLAYERS IN THE GAME MUST BE BETWEEN 2 AND 4 INCLUDED, YOU INSERTED " + num + " PLAYERS");
        }

        timers.stopTimer(player);
        transition(new JoinState(board, controller, disconnectingPlayers, num));
    }



    @Override
    public void placeStartingCard(String nickname, boolean placeOnFront) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO PLACE A STARTING CARD DURING CREATION STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO PLACE A STARTING CARD DURING CREATION STATE");
    }

    @Override
    public void chooseYourColor(String nickname, PlayerColor color) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO CHOOSE YOUR COLOR DURING CREATION STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO CHOOSE YOUR COLOR DURING CREATION STATE");
    }

    @Override
    public void chooseSecretObjective(String nickname, int choice) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO CHOOSE A SECRET OBJECTIVE DURING CREATION STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO CHOOSE A SECRET OBJECTIVE DURING CREATION STATE");
    }

    @Override
    public void draw(String nickname, char deckFrom, int cardPos) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO DRAW DURING CREATION STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO DRAW DURING CREATION STATE");
    }

    @Override
    public void placeCard(String nickname, String cardID, GamePoint cardPos, CornerDirection cornerDir, boolean placeOnFront) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO PLACE A CARD DURING CREATION STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO PLACE A CARD DURING CREATION STATE");
    }

    @Override
    public void restartGame (String nickname, int numOfPlayers) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO START ANOTHER GAME DURING CREATION STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO START ANOTHER GAME DURING CREATION STATE");
    }
}
