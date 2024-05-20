package it.polimi.ingsw.network.Commands;

import it.polimi.ingsw.controller.BoardController;

public class RestartGameCmd extends GameCommand{
    private final int numOfPlayers;
    public RestartGameCmd(BoardController gameController, String nickname, int numOfPlayers){
        super(gameController, nickname);
        this.numOfPlayers = numOfPlayers;
    }

    @Override
    public void execute() throws IllegalStateException {
        this.gameController.restartGame(nickname, numOfPlayers);
    }
}
