package it.polimi.ingsw.server.Commands;

import it.polimi.ingsw.controller.BoardController;
import it.polimi.ingsw.server.Commands.GameCommand;

import java.util.Random;

public class StartGameCmd extends GameCommand {
    public StartGameCmd(BoardController gameRef, String nickname) {
        super(gameRef, nickname);
    }

    @Override
    public void execute() throws IllegalStateException {
        int numOfPlayers = new Random().nextInt(4) + 1;
        gameController.restartGame(nickname, numOfPlayers);
    }
}
