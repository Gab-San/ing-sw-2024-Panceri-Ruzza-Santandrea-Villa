package it.polimi.ingsw.controller;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.PlayerColor;
import it.polimi.ingsw.server.VirtualClient;

public class JoinState extends GameState {
    int numOfPlayersToStart;

    public JoinState(Board board, int num){
        super(board);
        numOfPlayersToStart=num;
        board.setGamePhase(GamePhase.JP);
    }
    @Override
    public GameState join(String nickname, VirtualClient client) throws IllegalStateException {
        //should be impossible to be triggered, but in case of future of the addition of other phases, it may be necessary
        if(board.getGamePhase()!=GamePhase.JP)
            throw new IllegalStateException("IMPOSSIBLE TO JOIN IN THIS PHASE");
        board.addPlayer(new Player(nickname)); // throws exception if player can't be added
        board.getGameInfo().addClient(client);
        if(this.board.getPlayerAreas().size() < numOfPlayersToStart) {
            return this;
        } else return nextState();
    }

    @Override
    public GameState setNumOfPlayers(String nickname, int num) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO CHANGE THE NUMBER OF PLAYERS DURING JOIN STATE");
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
    public GameState draw(String nickname, char deckFrom, int cardPos) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO DRAW STARTING CARDS DURING JOIN STATE");
    }

    @Override
    public GameState placeCard(String nickname, String cardID, Point cardPos, CornerDirection cornerDir, boolean placeOnFront) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO PLACE CARDS DURING JOIN STATE");
    }

    private GameState nextState() {
        return new SetupState(board);
    }

    @Override
    public GameState startGame (String nickname, int numOfPlayers) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO START A NEW GAME DURING JOIN STATE");
    }
}
