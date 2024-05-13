package it.polimi.ingsw.server.Commands;

import it.polimi.ingsw.controller.BoardController;

public class StartGameCmd extends GameCommand{
    private final int numPlayers;
    public StartGameCmd(BoardController gameController, String nickname, int numPlayers){
        super(gameController, nickname);
        this.numPlayers = numPlayers;
    }

    @Override
    public void execute() throws IllegalStateException {
        //FIXME: See github issue on startGame (for now adding parameter to this cmd)
        this.gameController.startGame(nickname, numPlayers);
    }
}
