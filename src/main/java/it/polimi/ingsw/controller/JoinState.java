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

    public JoinState(Board board, BoardController controller, int num){
        super(board, controller);
        numOfPlayersToStart=num;
        board.setGamePhase(GamePhase.JOIN);
    }
    @Override
    public void join(String nickname, VirtualClient client) throws IllegalStateException {
        //should be impossible to be triggered, but in case of future of the addition of other phases, it may be necessary
        if(board.getGamePhase()!=GamePhase.JOIN)
            throw new IllegalStateException("IMPOSSIBLE TO JOIN IN THIS PHASE");
        board.addPlayer(new Player(nickname)); // throws exception if player can't be added
        //TODO cambiare con iscrizione ai listener
        board.getGameInfo().addClient(client);
        if( !(this.board.getPlayerAreas().size() < numOfPlayersToStart) ) {
            transition(new SetupState(board, controller));
        }
    }

    @Override
    public void setNumOfPlayers(String nickname, int num) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO CHANGE THE NUMBER OF PLAYERS DURING JOIN STATE");
    }

    @Override
    public void disconnect(String nickname, VirtualClient client) throws IllegalStateException, IllegalArgumentException {
        board.removePlayer(nickname); // throws exception if player isn't in game
        board.getGameInfo().removeClient(client);
    }

    @Override
    public void placeStartingCard(String nickname, boolean placeOnFront) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO PLACE STARTING CARDS DURING JOIN STATE");
    }
    public void chooseYourColor(String nickname, PlayerColor color) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO CHOOSE YOUR COLOR DURING CREATION STATE");
    }

    @Override
    public void chooseSecretObjective(String nickname, int choice) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO CHOOSE SECRETE OBJECTIVE DURING JOIN STATE");
    }

    @Override
    public void draw(String nickname, char deckFrom, int cardPos) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO DRAW STARTING CARDS DURING JOIN STATE");
    }

    @Override
    public void placeCard(String nickname, String cardID, Point cardPos, CornerDirection cornerDir, boolean placeOnFront) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO PLACE CARDS DURING JOIN STATE");
    }

    @Override
    public void startGame (String nickname, int numOfPlayers) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO START A NEW GAME DURING JOIN STATE");
    }
}
