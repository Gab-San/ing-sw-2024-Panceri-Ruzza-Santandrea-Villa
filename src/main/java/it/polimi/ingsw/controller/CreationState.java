package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Point;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.PlayerColor;
import it.polimi.ingsw.server.VirtualClient;

public class CreationState extends GameState{

    public CreationState(Board board) {
        super(board);
        board.setGamePhase(GamePhase.SNOFP);
    }

    @Override
    public GameState join(String nickname, VirtualClient client) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO JOIN THE GAME DURING CREATION STATE");
    }

    @Override
    public GameState setNumOfPlayers(String nickname, int num) throws IllegalStateException, IllegalArgumentException {
        if(!board.getPlayerAreas().containsKey(board.getPlayerByNickname(nickname)))
            throw new IllegalArgumentException("PLAYER ISN'T IN GAME");
        if(num<2 || num>4)
            throw new IllegalArgumentException("NUMBER OF PLAYERS IN THE GAME MUST BE BETWEEN 2 AND 4 INCLUDED, YOU INSERTED " + num + "PLAYERS");
        return this.nextState(num);
    }

    @Override
    public void placeStartingCard(String nickname, boolean placeOnFront) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO PLACE A STARTING CARD DURING CREATION STATE");
    }

    @Override
    public void chooseYourColor(String nickname, PlayerColor color) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO CHOOSE YOUR COLOR DURING CREATION STATE");
    }

    @Override
    public GameState chooseSecretObjective(String nickname, int choice) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO CHOOSE A SECRET OBJECTIVE DURING CREATION STATE");
    }

    @Override
    public GameState draw(String nickname, char deckFrom, int cardPos) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO DRAW DURING CREATION STATE");
    }

    @Override
    public GameState placeCard(String nickname, String cardID, Point cardPos, CornerDirection cornerDir, boolean placeOnFront) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO PLACE A CARD DURING CREATION STATE");
    }
    private GameState nextState(int num) throws IllegalStateException {
            return new JoinState(board, num);
        }

    @Override
    public GameState startGame (String nickname, int numOfPlayers) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO START ANOTHER GAME DURING CREATION STATE");
    }
}
