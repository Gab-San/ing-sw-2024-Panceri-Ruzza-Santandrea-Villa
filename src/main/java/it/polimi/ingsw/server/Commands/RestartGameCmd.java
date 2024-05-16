package it.polimi.ingsw.server.Commands;

import it.polimi.ingsw.controller.BoardController;

public class RestartGameCmd extends GameCommand{
    private final int numPlayers;
    public RestartGameCmd(BoardController gameController, String nickname, int numPlayers){
        super(gameController, nickname);
        this.numPlayers = numPlayers;
    }

    @Override
    public void execute() throws IllegalStateException {
        this.gameController.restartGame(nickname, numPlayers);
    }
}
