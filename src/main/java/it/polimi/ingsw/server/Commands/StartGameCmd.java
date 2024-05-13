package it.polimi.ingsw.server.Commands;

import it.polimi.ingsw.controller.BoardController;
import it.polimi.ingsw.server.Commands.GameCommand;
import java.util.Random;


public class StartGameCmd extends GameCommand{
    private final int numOfPlayers;
    public StartGameCmd(BoardController gameController, String nickname, int numOfPlayers){
        super(gameController, nickname);
        this.numOfPlayers = numOfPlayers;
    }

    @Override
    public void execute() throws IllegalStateException {
        this.gameController.restartGame(nickname, numOfPlayers);
    }
}
