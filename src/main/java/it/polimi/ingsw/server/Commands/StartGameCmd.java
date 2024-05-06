package it.polimi.ingsw.server.Commands;

import it.polimi.ingsw.controller.BoardController;

public class StartGameCmd extends GameCommand{

    public StartGameCmd(BoardController gameController, String nickname){
        super(gameController, nickname);
    }

    @Override
    public void execute() throws IllegalStateException {
        this.gameController.startGame(nickname);
    }
}
