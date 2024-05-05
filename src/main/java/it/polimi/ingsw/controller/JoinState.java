package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.PlayerColor;
import it.polimi.ingsw.server.VirtualClient;

import java.util.HashSet;
import java.util.Set;

public class JoinState extends GameState {
    private final Set<String> readyPlayers;
    int numOfPlayersToStart;
    public JoinState(Board board){
        super(board);
        readyPlayers = new HashSet<>();
        board.setGamePhase(GamePhase.JP);
    }

    public JoinState(Board board, int num){
        super(board);
        readyPlayers = new HashSet<>();
        numOfPlayersToStart=num;
    }
    @Override
    public GameState join(String nickname, VirtualClient client) throws IllegalStateException {
        board.addPlayer(new Player(nickname)); // throws exception if player can't be added
        board.getGameInfo().addClient(client);
        if(this.board.getPlayerAreas().size()>numOfPlayersToStart) {
            JoinState joinState = this;
            return joinState;
        } else return nextState();
    }

    @Override
    public GameState setNumOfPlayers(String nickname, int num) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO CHANGE THE NUMBER OF PLAYERS DURING JOIN STATE");
    }

    @Override
    public GameState startGame(String nickname) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO START A NEW GAME DURING JOIN STATE");
    }

    @Override
    public void placeStartingCard(String nickname, boolean placeOnFront) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO PLACE STARTING CARDS DURING JOIN STATE");
    }
    public void chooseYourColor(String nickname, PlayerColor color) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO CHOOSE YOUR COLOR DURING CREATION STATE");
    }

    @Override
    public GameState chooseSecretObjective(String nickname, int choice) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO CHOOSE SECRETE OBJECTIVE DURING JOIN STATE");
    }

    @Override
    public GameState draw(String nickname, String cardToDraw) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO DRAW STARTING CARDS DURING JOIN STATE");
    }

    @Override
    public void placeCard(String nickname, PlayCard card, Corner corner) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO PLACE CARDS DURING JOIN STATE");
    }

    private GameState nextState() throws IllegalStateException {
        return new SetupState(board);
    }
}
