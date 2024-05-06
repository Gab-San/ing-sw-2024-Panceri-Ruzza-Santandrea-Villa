package it.polimi.ingsw.server.Commands;

import it.polimi.ingsw.controller.BoardController;

public class SetNumOfPlayersCmd extends GameCommand{
    private final int numOfPlayers;
    public SetNumOfPlayersCmd(BoardController gameController, String nickname, int num){
        super(gameController, nickname);
        numOfPlayers = num;
    }
    @Override
    public void execute() throws IllegalStateException {
        this.gameController.setNumOfPlayers(nickname, numOfPlayers);
    }
}
